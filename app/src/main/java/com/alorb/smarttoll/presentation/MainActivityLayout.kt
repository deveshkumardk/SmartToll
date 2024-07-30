package com.alorb.smarttoll.presentation


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.alorb.smarttoll.R
import com.alorb.smarttoll.common.composable.MyTopAppBar
import com.alorb.smarttoll.common.composable.rememberMapViewWithLifecycle
import com.alorb.smarttoll.navigation.Screen
import com.alorb.smarttoll.presentation.anpr.AnprActivity
import com.alorb.smarttoll.presentation.expense.ExpenseActivity
import com.alorb.smarttoll.presentation.geofencing.GeofencingActivity
import com.alorb.smarttoll.presentation.geofencing.viewmodel.GeofenceViewModel
import com.alorb.smarttoll.presentation.gps.GpsActivity
import com.alorb.smarttoll.presentation.manual.ManualActivity
import com.alorb.smarttoll.presentation.profile.ProfileViewModel
import com.alorb.smarttoll.presentation.sos.SosActivity
import com.alorb.smarttoll.viewmodel.PreferencesViewModel
import com.mappls.sdk.maps.MapplsMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType",
    "StateFlowValueCalledInComposition"
)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityLayout(
    viewModel: PreferencesViewModel = hiltViewModel(),
    viewModelprofile: ProfileViewModel = hiltViewModel(),
    navController: NavController,
    geofenceViewModel: GeofenceViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    viewModel.setEmailID(context,viewModelprofile.emailID)
    viewModel.setPhotoUrl(context,viewModelprofile.photoUrl)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val coroutineScope = rememberCoroutineScope()
    val clickedLoc by remember {
        mutableStateOf("")
    }
    var payment by remember {
        mutableStateOf("100.00")
    }

    Scaffold(
        topBar = {
            MyTopAppBar(
                title = stringResource(id = R.string.app_name),
                navigationIcon = {
                    Box {
                        Image(
                            modifier = Modifier
                                .padding(horizontal = 7.dp)
                                .size(48.dp),
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            colorFilter = ColorFilter.tint(
                                color = MaterialTheme.colorScheme.primary,
                                blendMode = BlendMode.SrcAtop
                            )
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                actions = {
                    Box(
                        Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .clickable {
                                navController.navigate(Screen.ProfileScreen.route)
                            }
                    ) {
                        AsyncImage(
                            model = viewModelprofile.photoUrl,
                            contentDescription = "",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            )
        }
    ) {
        Box(
            Modifier.
            padding(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding()
            )
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MainActivityItem(name = "Manual") {
                       val intent = Intent(context, ManualActivity::class.java)
                       context.startActivity(intent)
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    MainActivityItem(name = "ANPR") {
                        val intent = Intent(context, AnprActivity::class.java)
                        context.startActivity(intent)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MainActivityItem(name = "Automatic (Geofencing)") {
                        val intent = Intent(context, GeofencingActivity::class.java)
                        context.startActivity(intent)
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    MainActivityItem(name = "Vehicle GPS") {
                        val intent = Intent(context, GpsActivity::class.java)
                        context.startActivity(intent)
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MainActivityItem(name = "Vehicle Expense") {
                        val intent = Intent(context, ExpenseActivity::class.java)
                        context.startActivity(intent)
                    }
                    Spacer(modifier = Modifier.width(20.dp))
                    MainActivityItem(name = "SOS") {
                        val intent = Intent(context, SosActivity::class.java)
                        context.startActivity(intent)
                    }

                }
//                MapScreen(
//                    payment = {
//                        payment = it
//                    }
//                )
            }
        }
    }
}

@Composable
fun MainActivityItem(
    name: String,
    click: () -> Unit,

    ){
    Box(
        modifier = Modifier
            .size(140.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFD174E0), // Light Purple
                        Color(0xFFF76497)  // Light Pink
                    ),
                    startY = 0f,
                    endY = 100f
                ),
                shape = RoundedCornerShape(5.dp)
            )
            .clickable {
                click()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun MapUI(context: Context) {
    val mapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    // on below line creating a
    // column for our maps.
    var mapmyIndiaMap by remember { mutableStateOf<MapplsMap?>(null) }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color.White)
    ) {
        // on below line adding a map view to it.
        AndroidView({ mapView }) { mapView ->
            // on below line launching our map view
            CoroutineScope(Dispatchers.Main).launch {
                val map = mapView.loadMap()
//                map.getMapAsync(this)
                // on below line adding zoom controls for map.
            }
            mapmyIndiaMap?.addOnMapClickListener {
                false
            }
        }
    }
}