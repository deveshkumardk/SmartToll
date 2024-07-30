package com.alorb.smarttoll.presentation.expense
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alorb.smarttoll.presentation.expense.ExpenseActivity.Companion.REQUEST_CODE_AMOUNT
import com.alorb.smarttoll.presentation.expense.ExpenseActivity.Companion.REQUEST_CODE_VEHICLE_NUMBER
import com.alorb.smarttoll.ui.theme.SmartTollTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import android.graphics.Paint as AndroidPaint

class ExpenseActivity : ComponentActivity() {
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var expenseViewModel: ExpenseViewModel

    private val speechRecognizerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            spokenText?.let { handleVoiceRecognitionResult(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        expenseViewModel = ViewModelProvider(this).get(ExpenseViewModel::class.java)

        setContent {
            SmartTollTheme {
                ExpenseScreen(expenseViewModel, onStartVoiceRecognition = ::startVoiceRecognition)
            }
        }
    }

    private var pendingRequestCode: Int? = null

    private fun startVoiceRecognition(requestCode: Int) {
        pendingRequestCode = requestCode
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }
        speechRecognizerLauncher.launch(intent)
    }

    private fun handleVoiceRecognitionResult(spokenText: String) {
        pendingRequestCode?.let { requestCode ->
            when (requestCode) {
                REQUEST_CODE_VEHICLE_NUMBER -> expenseViewModel.vehicleNumber = spokenText
                REQUEST_CODE_AMOUNT -> expenseViewModel.amount = spokenText
            }
            pendingRequestCode = null
        }
    }

    override fun onDestroy() {
        speechRecognizer.destroy()
        super.onDestroy()
    }

    companion object {
        const val REQUEST_CODE_VEHICLE_NUMBER = 1
        const val REQUEST_CODE_AMOUNT = 2
    }
}

data class Expense(val date: String, val category: String, val amount: Double)

class ExpenseViewModel : ViewModel() {
    var expenses by mutableStateOf(listOf<Expense>())
        private set
    var vehicleNumber by mutableStateOf("")
    var amount by mutableStateOf("")
    fun addExpense(date: String, category: String, amount: Double) {
        val newExpense = Expense(date, category, amount)
        expenses = expenses + newExpense
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(
    viewModel: ExpenseViewModel, onStartVoiceRecognition: (Int) -> Unit)
 {

    var selectedCategory by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var expenseType by remember { mutableStateOf("") }
    var vehicleNumberSet by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showChart by remember { mutableStateOf(false) } // Add this state variable

    val tollRelatedCategories = listOf(
        "Toll Tax",
        "Toll Violation Fine"
    )
    val carRelatedCategories = listOf(
        "Fuel", "Maintenance", "Driver Salary", "Insurance"


    )
    val categories = if (expenseType == "Toll") tollRelatedCategories else carRelatedCategories

    if (showChart) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color(0xFFF5F5F5))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { showChart = false }) { // Add back button functionality
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF6200EA)
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Expense Chart",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF6200EA),
                    modifier = Modifier.weight(2f)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(16.dp))
            ExpenseChart(expenses = viewModel.expenses)
        }
    } else {
        if (!vehicleNumberSet) {
            VehicleNumberInput(
                vehicleNumber = viewModel.vehicleNumber,
                onVehicleNumberChange = { viewModel.vehicleNumber = it },
                onConfirm = { vehicleNumberSet = true },
                onStartVoiceRecognition = { onStartVoiceRecognition(REQUEST_CODE_VEHICLE_NUMBER) }
            )
        } else {
            if (expenseType.isEmpty()) {
                ExpenseTypeSelection(
                    onTollRelated = { expenseType = "Toll" },
                    onCarRelated = { expenseType = "Car" }
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .background(Color(0xFFF5F5F5))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = { expenseType = "" }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color(0xFF6200EA)
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Expense Tracker",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color(0xFF6200EA),
                            modifier = Modifier.weight(2f)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Date") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clickable { showDatePicker = true },
                        enabled = false,
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color(0xFF6200EA)
                        )
                    )
                    if (showDatePicker) {
                        DatePickerDialog(onDismiss = { showDatePicker = false }) { selectedDate ->
                            date = selectedDate
                            showDatePicker = false
                        }
                    }

                    Text(
                        text = "Category",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                        color = Color(0xFF6200EA)
                    )
                    DropdownMenu(
                        categories = categories,
                        selectedCategory = selectedCategory,
                        onCategorySelected = { selectedCategory = it }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = viewModel.amount,
                        onValueChange = { viewModel.amount = it },
                        label = { Text("Amount") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        colors = TextFieldDefaults.textFieldColors(
                            focusedIndicatorColor = Color(0xFF6200EA)
                        ),
                        trailingIcon = {
                            IconButton(onClick = { onStartVoiceRecognition(REQUEST_CODE_AMOUNT) }) {
                                Icon(Icons.Default.Mic, contentDescription = "Voice Input", tint = Color(0xFF6200EA))
                            }
                        }
                    )

                    Button(
                        onClick = {

                            val formattedDate = if (date.isNotEmpty()) date else SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                                Date()
                            )
                            val amountValue = viewModel.amount.toDoubleOrNull() ?: 0.0
                            viewModel.addExpense(formattedDate, selectedCategory, amountValue)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA))
                    ) {
                        Text("Add Expense", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showChart = true }, // Show chart when this button is clicked
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA))
                    ) {
                        Text("Show Chart", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    ExpenseTable(expenses = viewModel.expenses)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VehicleNumberInput(
    vehicleNumber: String,
    onVehicleNumberChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onStartVoiceRecognition: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = vehicleNumber,
            onValueChange = onVehicleNumberChange,
            label = { Text("Enter Vehicle Number") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF6200EA)
            ),
            trailingIcon = {
                IconButton(onClick = onStartVoiceRecognition) {
                    Icon(Icons.Default.Mic, contentDescription = "Voice Input", tint = Color(0xFF6200EA))
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onConfirm,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA))
        ) {
            Text("Confirm Vehicle Number", color = Color.White)
        }
    }
}

@Composable
fun ExpenseTypeSelection(onTollRelated: () -> Unit, onCarRelated: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Expense Type",
            style = MaterialTheme.typography.headlineSmall,
            color = Color(0xFF6200EA)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onTollRelated,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA))
        ) {
            Text("Toll Related", color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onCarRelated,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EA))
        ) {
            Text("Car Related", color = Color.White)
        }
    }
}

@Composable
fun ExpenseTable(expenses: List<Expense>) {
    LazyColumn {
        items(expenses.size) { index ->
            val expense = expenses[index]
            Column(modifier = Modifier.padding(8.dp)) {
                Text(text = "Date: ${expense.date}", color = Color(0xFF6200EA))
                Text(text = "Category: ${expense.category}", color = Color(0xFF6200EA))
                Text(text = "Amount: ${expense.amount}", color = Color(0xFF6200EA))
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
            }
        }
    }
}

@Composable
fun DatePickerDialog(onDismiss: () -> Unit, onDateSelected: (String) -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
        ) {
            AndroidView(factory = { context ->
                DatePicker(context).apply {
                    val today = Calendar.getInstance()
                    init(
                        today.get(Calendar.YEAR),
                        today.get(Calendar.MONTH),
                        today.get(Calendar.DAY_OF_MONTH)
                    ) { _, year, month, day ->
                        val calendar = Calendar.getInstance().apply {
                            set(year, month, day)
                        }
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        onDateSelected(dateFormat.format(calendar.time))
                    }
                }
            })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenu(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = selectedCategory,
            onValueChange = { },
            label = { Text("Select Category") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            enabled = false,
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF6200EA)
            )
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun ExpenseChart(expenses: List<Expense>) {
    if (expenses.isEmpty()) {
        Text("No data to display", color = Color.Red)
        return
    }

    // Group expenses by category
    val expensesByCategory = expenses.groupBy { it.category }

    val maxAmount = expenses.maxOf { it.amount }
    val minAmount = expenses.minOf { it.amount }
    val yRange = maxAmount - minAmount
    val padding = 32.dp

    // Extract months from the dates and limit to the last 6 months
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val monthFormat = SimpleDateFormat("MMM", Locale.US)
    val months = expenses.map { expense ->
        monthFormat.format(dateFormat.parse(expense.date)!!)
    }.distinct().takeLast(6)

    // State for zoom level
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var isDragging by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.White)
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 3f)
                        if (isDragging) {
                            offset = offset + pan
                        }
                    }
                }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = { isDragging = true },
                        onTap = { isDragging = false }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val startX = padding.toPx()
                val startY = padding.toPx()
                val endX = size.width - padding.toPx()
                val endY = size.height - padding.toPx()

                val stepX = (endX - startX) / (months.size - 1).coerceAtLeast(1) * scale
                val scaleY = (endY - startY) / yRange.toFloat() * scale

                // Define colors for categories
                val categoryColors = listOf(Color.Blue, Color.Green, Color.Red, Color.Cyan, Color.Magenta)
                val paint = androidx.compose.ui.graphics.Paint().apply { isAntiAlias = true }
                val textPaint = AndroidPaint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = AndroidPaint.Align.CENTER
                    textSize = 24f
                }

                // Draw lines for each category
                expensesByCategory.entries.forEachIndexed { index, (category, categoryExpenses) ->

                    val path = androidx.compose.ui.graphics.Path()
                    val categoryColor = categoryColors[index % categoryColors.size]

                    val filteredCategoryExpenses = categoryExpenses
                        .filter { expense -> months.contains(monthFormat.format(dateFormat.parse(expense.date)!!)) }
                        .sortedBy { it.date }

                    filteredCategoryExpenses.forEachIndexed { i, expense ->
                        val x = startX + i * stepX + offset.x
                        val y = endY - (expense.amount - minAmount).toFloat() * scaleY + offset.y
                        if (i == 0) {
                            path.moveTo(x, y)
                        } else {
                            path.lineTo(x, y)
                        }
                    }

                    drawPath(
                        path = path,
                        color = categoryColor,
                        style = Stroke(width = 4f)
                    )
                }

                // Draw x-axis labels (months)
                months.forEachIndexed { i, month ->
                    val x = startX + i * stepX + offset.x
                    drawContext.canvas.nativeCanvas.drawText(
                        month,
                        x,
                        endY + 40, // Adjust this value to position the text below the x-axis
                        AndroidPaint().apply {
                            color = android.graphics.Color.BLACK
                            textAlign = AndroidPaint.Align.CENTER
                            textSize = 24f
                        }
                    )
                }

                // Draw y-axis labels (amounts)
                val numYLabels = 5
                for (i in 0..numYLabels) {
                    val y = endY - (i * (endY - startY) / numYLabels) + offset.y
                    val label = "%.2f".format(minAmount + i * yRange / numYLabels)
                    drawContext.canvas.nativeCanvas.drawText(
                        label,
                        startX - 40, // Adjust this value to position the text left of the y-axis
                        y,
                        AndroidPaint().apply {
                            color = android.graphics.Color.BLACK
                            textAlign = AndroidPaint.Align.RIGHT
                            textSize = 24f
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Draw legends below the chart
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = padding)) {
            expensesByCategory.entries.forEachIndexed { index, (category, _) ->
                val categoryColors =listOf(Color.Blue, Color.Green, Color.Red, Color.Cyan, Color.Magenta)
                val categoryColor = categoryColors[index % categoryColors.size]
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Canvas(modifier = Modifier.size(20.dp)) {
                        drawRect(color = categoryColor)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = category,
                        color = Color.Black,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
