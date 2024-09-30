package com.mike.hms.houses.bookHouse.housePayment

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.mike.hms.houses.formatNumber
import com.mike.hms.model.houseModel.HouseEntity
import com.mike.hms.model.userModel.UserEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun ReceiptDialog(
    context: Context,
    house: HouseEntity,
    user: UserEntity,
    paymentMethod: PaymentMethod?,
    onDismiss: () -> Unit
) {

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CC.primaryColor())
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    "Purchase Receipt",
                    style = CC.titleTextStyle().copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = CC.extraPrimaryColor()
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                HorizontalDivider(color = CC.extraPrimaryColor(), thickness = 2.dp)

                ReceiptItem("House", house.houseName)
                ReceiptItem("Price", "Ksh. ${formatNumber(house.housePrice)}")
                ReceiptItem("Payment Method", paymentMethod?.name ?: "N/A")
                ReceiptItem("Buyer", user.firstName)
                ReceiptItem(
                    "Date",
                    SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                )

                HorizontalDivider(color = CC.extraPrimaryColor(), thickness = 2.dp)

                Text(
                    "Thank you for your purchase!",
                    style = CC.bodyTextStyle().copy(
                        fontWeight = FontWeight.Bold,
                        color = CC.extraPrimaryColor()
                    ),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    "Enjoy your time in the house.",
                    style = CC.bodyTextStyle().copy(color = CC.extraPrimaryColor()),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "House Purchase Receipt")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    generateReceiptText(house, user, paymentMethod)
                                )
                            }
                            context.startActivity(
                                Intent.createChooser(
                                    shareIntent,
                                    "Share Receipt"
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CC.extraSecondaryColor())
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Share")
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = CC.extraSecondaryColor())
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}

@Composable
fun ReceiptItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = CC.bodyTextStyle().copy(fontWeight = FontWeight.Bold))
        Text(value, style = CC.bodyTextStyle())
    }
}

fun generateReceiptText(house: HouseEntity, user: UserEntity, paymentMethod: PaymentMethod?): String {
    return """
        Purchase Receipt
        
        House: ${house.houseName}
        Price: Ksh. ${formatNumber(house.housePrice)}
        Payment Method: ${paymentMethod?.name ?: "N/A"}
        Buyer: ${user.firstName}
        Date: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())}
        
        Thank you for your purchase! Enjoy your time in the house.
    """.trimIndent()
}