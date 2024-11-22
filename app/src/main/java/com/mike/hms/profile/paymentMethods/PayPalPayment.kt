package com.mike.hms.profile.paymentMethods

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mike.hms.model.userModel.UserEntity
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun AddPayPalPayment(
    userEntity: UserEntity,
    onDismiss: () -> Unit = {}
) {
    var paypalEmail by remember { mutableStateOf("") }
    var useCurrentEmail by remember { mutableStateOf(true) }

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
                "Add PayPal Payment",
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

        Spacer(modifier = Modifier.height(16.dp))

        // PayPal Information Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = CC.titleColor().copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Information",
                    tint = CC.titleColor(),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "When you make a booking, we'll send a PayPal invoice to your email. " +
                            "You can then securely complete the payment through PayPal's platform.",
                    style = CC.contentTextStyle().copy(
                        fontSize = 14.sp,
                        color = CC.textColor().copy(alpha = 0.8f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Email Selection Options
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, CC.textColor().copy(alpha = 0.1f), RoundedCornerShape(8.dp))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { useCurrentEmail = true }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Use current email",
                        style = CC.contentTextStyle().copy(fontWeight = FontWeight.Medium)
                    )
                    Text(
                        userEntity.email,
                        style = CC.contentTextStyle().copy(
                            fontSize = 14.sp,
                            color = CC.textColor().copy(alpha = 0.7f)
                        )
                    )
                }
                RadioButton(
                    selected = useCurrentEmail,
                    onClick = { useCurrentEmail = true },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = CC.titleColor(),
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
                    .clickable { useCurrentEmail = false }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Use different PayPal email",
                    style = CC.contentTextStyle().copy(fontWeight = FontWeight.Medium)
                )
                RadioButton(
                    selected = !useCurrentEmail,
                    onClick = { useCurrentEmail = false },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = CC.titleColor(),
                        unselectedColor = CC.textColor().copy(alpha = 0.5f)
                    )
                )
            }
        }

        AnimatedVisibility(
            visible = !useCurrentEmail,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(16.dp))
                CC.MyOutlinedTextField(
                    value = paypalEmail,
                    onValueChange = { paypalEmail = it },
                    label = "PayPal Email Address",
                    singleLine = true,
                    keyboardType = KeyboardType.Email,
                    placeholder = "your.paypal@email.com",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
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
            onClick = { /* Handle add payment */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CC.titleColor()
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Payment,
                    contentDescription = "PayPal",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Set Up PayPal Payments",
                    style = CC.contentTextStyle().copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Security Note
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Security,
                contentDescription = "Secure",
                tint = CC.textColor().copy(alpha = 0.6f),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Payments are securely processed by PayPal",
                style = CC.contentTextStyle().copy(
                    fontSize = 12.sp,
                    color = CC.textColor().copy(alpha = 0.6f)
                )
            )
        }
    }
}