package com.alorb.smarttoll.presentation.auth.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alorb.smarttoll.R
import com.alorb.smarttoll.common.constants.Constants.SIGN_IN_WITH_GOOGLE

@Composable
fun SignInButton(
    onClick: () -> Unit
) {
    OutlinedButton(
        modifier = Modifier.padding(bottom = 48.dp),
        shape = RoundedCornerShape(6.dp),
        onClick = onClick
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.ic_google_logo
            ),
            contentDescription = null
        )
        Text(
            text = SIGN_IN_WITH_GOOGLE,
            modifier = Modifier.padding(6.dp),
            fontSize = 18.sp
        )
    }
}