
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speechnancial.data.firebase.model.Wallet
import com.example.speechnancial.tools.formatToThousandsSeparator
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun WalletItem(
    wallet: Wallet,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .border(
                BorderStroke(1.dp, Color.Gray),
                RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        Row (verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(
                    text = wallet.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                if (wallet.isDefaultWallet)
                    Text(
                        "Dompet Utama",
                        fontSize = 12.sp
                    )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Column (
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Rp ${formatToThousandsSeparator(wallet.balance)}", // Saldo ditampilkan
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                }
            }
        }
    }
}


@PreviewLightDark
@Composable
fun WalletItemPreview() {
    SpeechnancialTheme {
        Surface {
            WalletItem(
                wallet = Wallet(
                    name = "Wallet Utama",
                    balance = 5000f,
                    totalEarning = 20000f,
                    totalSpending = 250000f,
                    isDefaultWallet = true
                ),
                onClick = {}
            )
        }
    }
}