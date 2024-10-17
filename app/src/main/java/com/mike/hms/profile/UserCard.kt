package com.mike.hms.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mike.hms.model.userModel.UserEntity
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun UserCard(
    user: UserEntity,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = CC.surfaceContainerColor(),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
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
            text = user?.email ?: "N/A",
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
fun EditDetails(
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
){
    CC.MyOutlinedTextField(
        value = firstName,
        onValueChange = { onFirstNameChange(it) },
        label = "First Name",
        modifier = Modifier.fillMaxWidth(),
        placeholder = "John"
    )
    Spacer(modifier = Modifier.height(8.dp))
    CC.MyOutlinedTextField(
        value = lastName,
        onValueChange = { onLastNameChange(it) },
        label = "Last Name",
        modifier = Modifier.fillMaxWidth(),
        placeholder = "Doe"
    )
    Spacer(modifier = Modifier.height(8.dp))
    CC.MyOutlinedTextField(
        value = phoneNumber,
        onValueChange = { onPhoneNumberChange(it) },
        label = "Phone Number",
        keyboardType = KeyboardType.Phone,
        placeholder = "+254",
        modifier = Modifier.fillMaxWidth()
    )
}