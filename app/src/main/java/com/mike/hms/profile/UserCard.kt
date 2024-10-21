package com.mike.hms.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mike.hms.model.userModel.UserEntity
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun UserCard(
    user: UserEntity,
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = CC.surfaceContainerColor(),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        //Image Box
        Box(
            modifier = Modifier
                .size(screenHeight * 0.2f)
                .border(
                    1.dp, CC.textColor(), CircleShape
                )
        ){
            AsyncImage(
                model = user.photoUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(CircleShape)
                    .fillMaxSize()
            )
        }
        // Name Section
        Text(
            text = "Name",
            style = CC.bodyTextStyle()
                .copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
        )
        Text(
            text = "${user.firstName} ${user.lastName}",
            style = CC.contentTextStyle()
                .copy(color = CC.tertiaryColor())
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Email Section
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            thickness = 0.5.dp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Email",
            style = CC.bodyTextStyle()
                .copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
        )
        Text(
            text = user.email,
            style = CC.contentTextStyle()
                .copy(color = CC.tertiaryColor())
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Phone Section
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            thickness = 0.5.dp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Phone",
            style = CC.bodyTextStyle()
                .copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
        )
        Text(
            text = user.phoneNumber,
            style = CC.contentTextStyle()
                .copy(color = CC.tertiaryColor())
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Divider after all sections
        HorizontalDivider(
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            thickness = 0.5.dp
        )
    }
}


@Composable
fun EditDetails(user: UserEntity){
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val email by remember { mutableStateOf(user.email) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Edit Details",
            style = CC.titleTextStyle().copy(
                color = CC.extraPrimaryColor()
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

    CC.MyOutlinedTextField(
        value = firstName,
        onValueChange = { firstName = it },
        label = "First Name",
        modifier = Modifier.fillMaxWidth(),
        placeholder = "Enter First Name"
    )
    Spacer(modifier = Modifier.height(8.dp))

    CC.MyOutlinedTextField(
        value = lastName,
        onValueChange = { lastName = it },
        label = "Last Name",
        modifier = Modifier.fillMaxWidth(),
        placeholder = "Enter Last Name"
    )
    Spacer(modifier = Modifier.height(8.dp))

    CC.MyOutlinedTextField(
        value = phoneNumber,
        onValueChange = { phoneNumber = it },
        label = "Phone Number",
        keyboardType = KeyboardType.Phone,
        placeholder = "Enter Phone Number",
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))

    CC.MyOutlinedTextField(
        value = email,
        onValueChange = {},
        label = "Email",
        keyboardType = KeyboardType.Email,
        placeholder = "Enter Email",
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
     AddCreditCard()

    Button(
        onClick = { /* Handle save button click */ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = CC.primaryColor(),
            contentColor = CC.surfaceContainerColor()
        )
    ) {
        Text("Save", style = CC.contentTextStyle())
    }
        Spacer(modifier = Modifier.height(8.dp))
    }
}