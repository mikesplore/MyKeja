package com.mike.hms.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mike.hms.model.userModel.CreditCardWithUser
import com.mike.hms.ui.theme.CommonComponents as CC

@Composable
fun CreditCard(
    creditCardWithUser: CreditCardWithUser
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = CC.surfaceContainerColor()
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Card Number
            Text(
                text = creditCardWithUser.creditCard.cardNumber,
                fontSize = 24.sp,
                color = CC.textColor(),
                modifier = Modifier.padding(bottom = 16.dp),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Card Holder Name
                Column {
                    Text(
                        text = "Card Holder",
                        fontSize = 12.sp,
                        color = CC.textColor().copy(alpha = 0.7f)
                    )
                    Text(
                        text = "${creditCardWithUser.user.firstName} ${creditCardWithUser.user.lastName}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CC.textColor()
                    )
                }

                // Expiry Date
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Expiry Date",
                        fontSize = 12.sp,
                        color = CC.textColor().copy(alpha = 0.7f)
                    )
                    Text(
                        text = creditCardWithUser.creditCard.expiryDate,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = CC.textColor()
                    )
                }
            }
        }
    }
}
