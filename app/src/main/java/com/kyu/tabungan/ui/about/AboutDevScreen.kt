package com.kyu.tabungan.ui.about

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kyu.tabungan.R
import com.kyu.tabungan.components.NeoButton
import com.kyu.tabungan.components.NeoCard
import com.kyu.tabungan.components.NeoIconButton
import com.kyu.tabungan.components.icons.NeoIcons
import com.kyu.tabungan.theme.Background
import com.kyu.tabungan.theme.BorderColor
import com.kyu.tabungan.theme.BrightBlue
import com.kyu.tabungan.theme.HardShadowColor
import com.kyu.tabungan.theme.LightBlue
import com.kyu.tabungan.theme.Surface
import com.kyu.tabungan.theme.TextMain
import com.kyu.tabungan.theme.TextMuted

@Composable
fun AboutDevScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                NeoIconButton(
                    icon = NeoIcons.ChevronLeft,
                    onClick = onNavigateBack,
                    contentDescription = "Kembali",
                    size = 40.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Informasi",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted
                    )
                    Text(
                        text = "Tentang Pengembang",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = TextMain
                    )
                }
            }
        }

        item {
            NeoCard(
                modifier = Modifier.fillMaxWidth(),
                shadowOffset = 6.dp,
                cornerRadius = 16.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val avatarShape = RoundedCornerShape(16.dp)
                    Box(modifier = Modifier.padding(end = 4.dp, bottom = 4.dp)) {
                        Box(
                            modifier = Modifier
                                .size(96.dp)
                                .offset(x = 4.dp, y = 4.dp)
                                .background(HardShadowColor, shape = avatarShape)
                        )
                        Image(
                            painter = painterResource(id = R.drawable.dev_profile),
                            contentDescription = "Foto Pengembang Kyuu",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(96.dp)
                                .clip(avatarShape)
                                .background(Surface)
                                .border(width = 2.5.dp, color = BorderColor, shape = avatarShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Kyuu",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = TextMain
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(LightBlue)
                            .border(width = 1.5.dp, color = BorderColor, shape = RoundedCornerShape(6.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Pengembang Aplikasi Tabungan",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrightBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Aplikasi Tabungan dirancang untuk membantu pencatatan keuangan pribadi dengan cepat, aman, dan tanpa ketergantungan server atau internet.",
                        fontSize = 13.sp,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        item {
            Text(
                text = "Tautan & Saluran Resmi",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = TextMain,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        item {
            DevLinkCard(
                title = "Saluran WhatsApp",
                subtitle = "Ikuti update, diskusi, dan rilis aplikasi terbaru",
                actionLabel = "Gabung Saluran",
                badgeText = "Komunitas",
                badgeBgColor = Color(0xFFE8F9EE),
                badgeTextColor = Color(0xFF19B96B),
                onClick = { openUrl("https://whatsapp.com/channel/0029VbDO8tI2phHLTSN2ed0U") }
            )
        }

        item {
            DevLinkCard(
                title = "YouTube @mommykyuu",
                subtitle = "Channel YouTube resmi Mommy Kyuu",
                actionLabel = "Buka YouTube",
                badgeText = "YouTube",
                badgeBgColor = Color(0xFFFFEBEB),
                badgeTextColor = Color(0xFFFF2A2A),
                onClick = { openUrl("https://youtube.com/@mommykyuu?si=9UkOqDuV5sz87X2u") }
            )
        }

        item {
            DevLinkCard(
                title = "GitHub @KyuuX444",
                subtitle = "Lihat profil pengembang dan portofolio proyek lainnya",
                actionLabel = "Buka Profil",
                badgeText = "GitHub",
                badgeBgColor = LightBlue,
                badgeTextColor = BrightBlue,
                onClick = { openUrl("https://github.com/KyuuX444") }
            )
        }

        item {
            DevLinkCard(
                title = "Website API",
                subtitle = "Layanan API dan integrasi web resmi api.kyzzz.xyz",
                actionLabel = "Buka Website",
                badgeText = "API",
                badgeBgColor = Color(0xFFF3E8FF),
                badgeTextColor = Color(0xFF7E22CE),
                onClick = { openUrl("https://api.kyzzz.xyz") }
            )
        }

        item {
            DevLinkCard(
                title = "Repositori Tabungan",
                subtitle = "Kode sumber, kontribusi, dan issue pelaporan bug",
                actionLabel = "Buka Repositori",
                badgeText = "Open Source",
                badgeBgColor = Color(0xFFFFF3CD),
                badgeTextColor = Color(0xFF856404),
                onClick = { openUrl("https://github.com/KyuuX444/tabungan") }
            )
        }

        item {
            NeoCard(
                modifier = Modifier.fillMaxWidth(),
                shadowOffset = 4.dp,
                cornerRadius = 12.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Tabungan v1.0.4",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = TextMain
                    )
                    Text(
                        text = "Dilisensikan di bawah MIT License. Seluruh data keuangan Anda tersimpan 100% lokal pada perangkat ini.",
                        fontSize = 12.sp,
                        color = TextMuted,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DevLinkCard(
    title: String,
    subtitle: String,
    actionLabel: String,
    badgeText: String,
    badgeBgColor: Color,
    badgeTextColor: Color,
    onClick: () -> Unit
) {
    NeoCard(
        modifier = Modifier.fillMaxWidth(),
        shadowOffset = 4.dp,
        cornerRadius = 12.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = TextMain
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(badgeBgColor)
                        .border(width = 1.dp, color = BorderColor, shape = RoundedCornerShape(6.dp))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = badgeText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = badgeTextColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                NeoButton(
                    text = actionLabel,
                    onClick = onClick,
                    shadowOffset = 2.dp,
                    borderWidth = 1.5.dp,
                    cornerRadius = 8.dp
                )
            }
        }
    }
}
