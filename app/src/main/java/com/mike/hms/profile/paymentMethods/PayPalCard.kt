package com.mike.hms.profile.paymentMethods

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.mike.hms.model.paymentMethods.PayPalWithUser
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun SavedPayPalCard(
    payPalEntity: PayPalWithUser,
    onDelete: () -> Unit = {},
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    val brush= Brush.horizontalGradient(
        colors = listOf(
            CC.secondaryColor(),
            CC.extraSecondaryColor()
        )
    )

    if (showConfirmDialog){
        CC.ConfirmDialog(
            title = "Delete PayPal Account",
            message = "Are you sure you want to delete this payment method?",
            onConfirm = {
                onDelete()
                showConfirmDialog = false
                },
            onDismiss = {
                showConfirmDialog = false
            }
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush)
        ){
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // PayPal Logo/Icon placeholder
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = "https://w7.pngwing.com/pngs/632/1015/png-transparent-paypal-logo-computer-icons-payment-paypal-blue-angle-service-thumbnail.png",
                            contentDescription = "PayPal Logo",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "PayPal",
                        style = CC.titleTextStyle().copy(
                            color = CC.primaryColor(),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        showConfirmDialog = true
                    } ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Account Name",
                        style = CC.contentTextStyle().copy(
                            color = CC.primaryColor(),
                            fontSize = 14.sp
                        )
                    )
                    Text(
                        payPalEntity.user.firstName + " " + payPalEntity.user.lastName,
                        style = CC.contentTextStyle().copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Email Address",
                        style = CC.contentTextStyle().copy(
                            color = CC.primaryColor(),
                            fontSize = 14.sp
                        )
                    )
                    Text(
                        payPalEntity.paypal.paypalEmail,
                        style = CC.contentTextStyle().copy(
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Secure",
                    tint = Color(0xFF003087),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    "Verified PayPal Account",
                    style = CC.contentTextStyle().copy(
                        fontSize = 12.sp,
                        color = CC.tertiaryColor().copy(0.9f)
                    )
                )
            }
        }
    }}
}