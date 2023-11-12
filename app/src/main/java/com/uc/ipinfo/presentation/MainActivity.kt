package com.uc.ipinfo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uc.ipinfo.R
import com.uc.ipinfo.domain.model.IpInfo
import com.uc.ipinfo.domain.model.Status
import com.uc.ipinfo.ui.theme.IpInfoTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IpInfoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    var ipAddress by remember { mutableStateOf("") }
    var isIpValid by remember { mutableStateOf(true) }

    val ipInfo by viewModel.data.observeAsState()
    val isSearching by viewModel.searching.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.ip_info_lookup),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = ipAddress,
            onValueChange = {
                ipAddress = it
                isIpValid = isValidIp(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MaterialTheme.colorScheme.background, shape = RectangleShape)
                .shadow(1.dp, shape = RectangleShape),
            textStyle = LocalTextStyle.current.copy(color = Color.Black),
            placeholder = { Text(stringResource(R.string.enter_ip_address)) },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            singleLine = true,
            isError = !isIpValid
        )
        if (!isIpValid) {
            Text(stringResource(R.string.invalid_ip_address), color = Color.Red)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isIpValid) {
                    viewModel.getIpInfo(ipAddress)
                }
            },
            enabled = !(isSearching ?: false) && isIpValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ),
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clip(RectangleShape)
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Icon(imageVector = Icons.Default.Send, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text(stringResource(R.string.submit))
        }

        Spacer(modifier = Modifier.height(16.dp))

        ipInfo?.let {
            when(it.status) {
                Status.FAILURE -> {
                    Text(stringResource(R.string.error, it.message), color = Color.Red)
                }
                Status.SUCCESS -> {
                    IpInfoLookupResultCard(it)
                }
            }
        }
    }
}

fun isValidIp(ip: String): Boolean {
    val ipPattern = Pattern.compile(
        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$"
    )
    return ipPattern.matcher(ip).matches()
}

@Composable
fun IpInfoLookupResultCard(ipInfo: IpInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = stringResource(R.string.ip_info_lookup_result), style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(stringResource(R.string.city, ipInfo.city))
            Text(stringResource(R.string.region, ipInfo.regionName))
            Text(stringResource(R.string.country, ipInfo.country))
            Text(stringResource(R.string.zip_code, ipInfo.zip))
            Text(stringResource(R.string.latitude, ipInfo.lat))
            Text(stringResource(R.string.longitude, ipInfo.lon))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IpInfoTheme {
        MainScreen()
    }
}