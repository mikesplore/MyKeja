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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mike.hms.model.paymentMethods.MpesaWithUser
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun SavedMpesaCard(
    mpesaEntity: MpesaWithUser,
    onDelete: () -> Unit = {},
) {
    var showConfirmDialog by remember { mutableStateOf(false) }
    if (showConfirmDialog) {
        CC.ConfirmDialog(
            title = "Delete M-PESA number",
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
    val brush = Brush.horizontalGradient(
        colors = listOf(CC.tertiaryColor().copy(0.5f), CC.extraSecondaryColor())
    )
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
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // M-PESA Logo/Icon placeholder
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF4CAF50).copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "M",
                            style = CC.titleTextStyle().copy(
                                color = Color(0xFF4CAF50),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        "M-PESA",
                        style = CC.titleTextStyle().copy(
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
                    }) {
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
                            color = CC.textColor().copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    )
                    Text(
                        mpesaEntity.user.firstName + " " + mpesaEntity.user.lastName,
                        style = CC.contentTextStyle().copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "Phone Number",
                        style = CC.contentTextStyle().copy(
                            color = CC.textColor().copy(alpha = 0.6f),
                            fontSize = 14.sp
                        )
                    )
                    Text(
                        mpesaEntity.user.phoneNumber,
                        style = CC.contentTextStyle().copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                "Default payment method",
                style = CC.contentTextStyle().copy(
                    fontSize = 12.sp,
                    color = Color(0xFF4CAF50)
                ),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }}
}


