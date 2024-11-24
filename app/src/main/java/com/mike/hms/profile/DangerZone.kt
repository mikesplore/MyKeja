package com.mike.hms.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.mike.hms.model.userModel.UserEntity
import com.mike.hms.model.userModel.UserViewModel
import com.mike.hms.ui.theme.CommonComponents as CC
import kotlinx.coroutines.delay

@Composable
fun DangerZone(
    user: UserEntity,
    userViewModel: UserViewModel,
    context: Context,
    navController: NavController
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showVerificationDialog by remember { mutableStateOf(false) }
    var verificationText by remember { mutableStateOf("") }
    var attempts by remember { mutableIntStateOf(0) }
    val maxAttempts = 3
    var cooldownActive by remember { mutableStateOf(false) }
    var countdown by remember { mutableIntStateOf(30) }
    val auth = FirebaseAuth.getInstance()
    val email = auth.currentUser?.email
    val expectedText = remember { email?.uppercase() }

    // Countdown timer effect
    LaunchedEffect(cooldownActive) {
        while (cooldownActive && countdown > 0) {
            delay(1000)
            countdown--
            if (countdown == 0) {
                cooldownActive = false
                attempts = 0
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Danger Zone Header
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = CC.extraSecondaryColor()
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    "Account Deletion",
                    style = CC.titleTextStyle().copy(
                        color = CC.textColor(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "This action cannot be undone. Please be certain.",
                    style = CC.contentTextStyle().copy(
                        color = CC.secondaryColor().copy(alpha = 0.7f)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFDC3545)
                    ),
                    enabled = !cooldownActive,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (cooldownActive) {
                        Text(
                            "Try again in ${countdown}s",
                            color = Color.White
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Delete Account",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Initial Delete Dialog
        if (showDeleteDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = {
                    Text(
                        "Delete Account",
                        style = CC.titleTextStyle().copy(fontWeight = FontWeight.Bold)
                    )
                },
                text = {
                    Column {
                        Text(
                            "This will permanently delete your account and all associated data. This action cannot be undone.",
                            style = CC.contentTextStyle()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "• All your bookings history will be deleted\n" +
                                    "• Your payment methods will be removed\n" +
                                    "• Your profile information will be erased",
                            style = CC.contentTextStyle()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showDeleteDialog = false
                            showVerificationDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC3545))
                    ) {
                        Text("Continue", style = CC.contentTextStyle().copy(color = Color.White))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel", style = CC.contentTextStyle())
                    }
                }
            )
        }

        // Verification Dialog
        if (showVerificationDialog) {
            AlertDialog(
                onDismissRequest = { showVerificationDialog = false },
                title = {
                    Text(
                        "Verify Deletion",
                        style = CC.titleTextStyle().copy(fontWeight = FontWeight.Bold)
                    )
                },
                text = {
                    Column {
                        Text(
                            "To confirm deletion, please type your Email in the field below:",
                            style = CC.contentTextStyle()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = verificationText,
                            onValueChange = { verificationText = it.uppercase() },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFDC3545),
                                unfocusedBorderColor = CC.textColor().copy(alpha = 0.5f)
                            )
                        )
                        if (attempts > 0) {
                            Text(
                                "Attempts remaining: ${maxAttempts - attempts}",
                                style = CC.contentTextStyle().copy(
                                    color = Color(0xFFDC3545),
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (verificationText == expectedText) {
                                userViewModel.deleteUser(user.userID) { success ->
                                    if (success) {
                                        Toast.makeText(context, "Account deleted successfully", Toast.LENGTH_SHORT).show()
                                        //sign out user
                                        auth.signOut()
                                    } else {
                                        Toast.makeText(context, "Failed to delete account", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                showVerificationDialog = false
                            } else {
                                attempts++
                                if (attempts >= maxAttempts) {
                                    cooldownActive = true
                                    countdown = 30
                                    showVerificationDialog = false
                                    verificationText = ""
                                }
                                Toast.makeText(
                                    context,
                                    "Incorrect verification text",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC3545))
                    ) {
                        Text("Delete My Account", style = CC.contentTextStyle().copy(color = Color.White))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showVerificationDialog = false
                            verificationText = ""
                        }
                    ) {
                        Text("Cancel", style = CC.contentTextStyle())
                    }
                }
            )
        }
    }
}