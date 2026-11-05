package com.example.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanTeal
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.NavyDark
import com.example.ui.theme.RoyalBlue
import com.example.ui.theme.SoftPurple
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodels.AiActionProposal
import com.example.viewmodels.AiChatMessage
import com.example.viewmodels.KaamKhataViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAssistantScreen(
    viewModel: KaamKhataViewModel,
    onBack: () -> Unit
) {
    val messages by viewModel.chatMessages.collectAsState()
    var inputQuery by remember { mutableStateOf("") }

    val quickQuestions = listOf(
        "आज मेरी हाजिरी लगा दो",
        "इस महीने कितनी सैलरी बनी?",
        "मेरा कितना पैसा बाकी है?"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "AI Assistant",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Right Pill: ✦ Gemini matching reference Screen 9
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF7F52FF), Color(0xFF9D6AFF))
                                )
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Gemini",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color(0xFF0A1329)
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF091226),
                            Color(0xFF0E1D3E),
                            Color(0xFF142750),
                            Color(0xFF0B1428)
                        )
                    )
                )
        ) {
            // Ambient Starry Particles Canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Star dots
                val stars = listOf(
                    Offset(w * 0.15f, h * 0.12f),
                    Offset(w * 0.85f, h * 0.18f),
                    Offset(w * 0.35f, h * 0.28f),
                    Offset(w * 0.75f, h * 0.40f),
                    Offset(w * 0.20f, h * 0.65f),
                    Offset(w * 0.90f, h * 0.72f),
                    Offset(w * 0.45f, h * 0.80f)
                )
                stars.forEach { pos ->
                    drawCircle(color = Color.White.copy(alpha = 0.6f), radius = 2.dp.toPx(), center = pos)
                }

                // Glowing blue halo behind robot
                drawCircle(
                    brush = Brush.radialGradient(
                        listOf(RoyalBlue.copy(alpha = 0.3f), Color.Transparent),
                        center = Offset(w * 0.5f, h * 0.22f),
                        radius = 160.dp.toPx()
                    ),
                    radius = 160.dp.toPx(),
                    center = Offset(w * 0.5f, h * 0.22f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(top = 10.dp, bottom = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 1. Cute 3D AI Robot Illustration waving "Hello! 👋" matching reference Screen 9
                    item {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier.size(130.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                // Robot Body & Head Container
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    // Antennas with glowing cyan tips
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(28.dp),
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(CyanTeal))
                                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(CyanTeal))
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))

                                    // Robot Head
                                    Box(
                                        modifier = Modifier
                                            .size(76.dp, 56.dp)
                                            .clip(RoundedCornerShape(22.dp))
                                            .background(Color.White)
                                            .shadow(8.dp, RoundedCornerShape(22.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        // Blue visor
                                        Box(
                                            modifier = Modifier
                                                .size(60.dp, 36.dp)
                                                .clip(RoundedCornerShape(16.dp))
                                                .background(Color(0xFF14243A)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                                // Glowing cyan eyes
                                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(CyanTeal))
                                                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(CyanTeal))
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    // Robot Body
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Waving arm
                                        Box(modifier = Modifier.size(14.dp, 24.dp).clip(RoundedCornerShape(6.dp)).background(Color.White))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .size(54.dp, 40.dp)
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(Color.White),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Box(modifier = Modifier.size(14.dp).clip(CircleShape).background(RoyalBlue))
                                        }
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Box(modifier = Modifier.size(14.dp, 24.dp).clip(RoundedCornerShape(6.dp)).background(Color.White))
                                    }
                                }
                            }
                        }
                    }

                    // 2. White Speech Dialogue Card matching reference Screen 9
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Hello! 👋",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Ask me anything about\nyour attendance, salary\nor payments.",
                                    fontSize = 13.sp,
                                    color = TextSecondary,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }

                    // 3. Quick Prompt Question Cards matching reference Screen 9
                    items(quickQuestions) { question ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.processAiQuery(question) },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = ">",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RoyalBlue
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = question,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    // 4. Chat History & Action Confirmation Cards if user asked questions
                    items(messages.drop(1)) { msg ->
                        ChatBubbleItem(
                            message = msg,
                            onConfirm = { proposal -> viewModel.confirmAiAction(proposal) }
                        )
                    }
                }

                // 5. Bottom Rounded Input Pill Bar matching reference Screen 9
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Mic",
                            tint = TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        OutlinedTextField(
                            value = inputQuery,
                            onValueChange = { inputQuery = it },
                            placeholder = { Text("Type your question...", fontSize = 13.sp, color = TextSecondary) },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.Transparent,
                                unfocusedBorderColor = Color.Transparent
                            )
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        // Blue Send Circular Button with White Arrow
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(RoyalBlue)
                                .clickable {
                                    if (inputQuery.isNotBlank()) {
                                        viewModel.processAiQuery(inputQuery)
                                        inputQuery = ""
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Send",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChatBubbleItem(
    message: AiChatMessage,
    onConfirm: (AiActionProposal) -> Unit
) {
    val isUser = message.sender == "USER"
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isUser) Alignment.End else Alignment.Start
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isUser) RoyalBlue else Color.White
            )
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = message.text,
                    fontSize = 13.sp,
                    color = if (isUser) Color.White else TextPrimary
                )

                if (message.proposal != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF4F7FC))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = message.proposal.title,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = message.proposal.description,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { onConfirm(message.proposal) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("कन्फर्म करें और खाते में जोड़ें", fontSize = 11.sp, color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}
