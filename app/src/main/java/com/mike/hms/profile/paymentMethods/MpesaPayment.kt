package com.mike.hms.profile.paymentMethods

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mike.hms.model.paymentMethods.MpesaEntity
import com.mike.hms.model.paymentMethods.MpesaViewModel
import com.mike.hms.model.userModel.UserEntity
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun AddMpesaPayment(
    userEntity: UserEntity,
    mpesaViewModel: MpesaViewModel,
    context: Context,
    onDismiss: () -> Unit = {}
) {
    var mpesaNumber by remember { mutableStateOf("") }
    var useCurrentNumber by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CC.primaryColor())
            .padding(20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Add M-PESA Payment",
                style = CC.titleTextStyle().copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = CC.textColor()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Number Selection Options
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, CC.textColor().copy(alpha = 0.1f), RoundedCornerShape(8.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { useCurrentNumber = true }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Use current number",
                        style = CC.contentTextStyle().copy(fontWeight = FontWeight.Medium)
                    )
                    Text(
                        userEntity.phoneNumber,
                        style = CC.contentTextStyle().copy(
                            fontStyle = FontStyle.Italic,
                            fontSize = 14.sp,
                            color = CC.tertiaryColor().copy(alpha = 0.7f)
                        )
                    )
                }
                RadioButton(
                    selected = useCurrentNumber,
                    onClick = { useCurrentNumber = true },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = CC.secondaryColor(),
                        unselectedColor = CC.textColor().copy(alpha = 0.5f)
                    )
                )
            }

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = CC.textColor().copy(alpha = 0.1f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { useCurrentNumber = false }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Use different number",
                    style = CC.contentTextStyle().copy(fontWeight = FontWeight.Medium)
                )
                RadioButton(
                    selected = !useCurrentNumber,
                    onClick = { useCurrentNumber = false },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = CC.secondaryColor(),
                        unselectedColor = CC.textColor().copy(alpha = 0.5f)
                    )
                )
            }
        }

        AnimatedVisibility(
            visible = !useCurrentNumber,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                CC.MyOutlinedTextField(
                    value = mpesaNumber,
                    onValueChange = { mpesaNumber = it },
                    label = "M-PESA Number",
                    singleLine = true,
                    keyboardType = KeyboardType.Phone,
                    placeholder = "+254712345678",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Phone",
                            tint = CC.textColor().copy(alpha = 0.5f)
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Add Payment Button
        Button(
            onClick = {
                if (!useCurrentNumber && mpesaNumber.isEmpty()) {
                    Toast.makeText(context, "Please enter your M-PESA number", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                loading = true
                CC.generateMpesaId { id ->
                    val mpesa = MpesaEntity(
                        mpesaId = id,
                        phoneNumber = if (useCurrentNumber) userEntity.phoneNumber else mpesaNumber,
                        userId = userEntity.userID,
                    )
                    mpesaViewModel.addMpesa(mpesa){success ->
                        if(success) {
                            loading = false
                            Toast.makeText(context, "M-PESA payment added successfully", Toast.LENGTH_SHORT).show()
                            mpesaViewModel.getMpesa(userEntity.userID)
                            onDismiss()
                        }
                        else {
                            loading = false
                            Toast.makeText(context, "Failed to add M-PESA payment. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CC.titleColor()
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            if (loading) {
                CircularProgressIndicator(
                    color = CC.primaryColor(),
                    strokeWidth = 1.dp,
                    modifier = Modifier.size(24.dp)
                )
            }
            else{
                Text(
                    "Add M-PESA Payment",
                    style = CC.contentTextStyle().copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "By adding your M-PESA number, you agree to our terms of service",
            style = CC.contentTextStyle().copy(
                fontSize = 12.sp,
                color = CC.textColor().copy(alpha = 0.6f)
            ),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}