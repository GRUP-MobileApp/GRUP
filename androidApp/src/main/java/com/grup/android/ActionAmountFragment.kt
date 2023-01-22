package com.grup.android

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels
import com.grup.android.ui.apptheme.AppTheme
import com.grup.android.ui.h1Text
import com.grup.models.UserInfo

class ActionAmountFragment : Fragment() {
    private val mainViewModel: MainViewModel by navGraphViewModels(R.id.main_graph)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ActionAmountLayout(
                    mainViewModel = mainViewModel,
                    navController = findNavController()
                )
            }
        }
    }
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun ActionAmountLayout(
    mainViewModel: MainViewModel,
    navController: NavController
) {
    val nullableUserInfo: UserInfo? by mainViewModel.myUserInfo.collectAsStateWithLifecycle()
    val myUserInfo: UserInfo = nullableUserInfo!!
    var actionAmount: String by remember { mutableStateOf("0") }

    Scaffold(
        topBar = {
            ActionAmountTopAppBar(
                onBackPress = { navController.popBackStack() }
            )
        }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacing),
            modifier = Modifier
                .fillMaxHeight()
                .padding(padding)
                .background(AppTheme.colors.primary)
        ) {
            h1Text(
                text = "Balance: $${myUserInfo.userBalance}",
                color = AppTheme.colors.onSecondary,
                fontSize = 50.sp,
            )
            h1Text(
                text = "$$actionAmount",
                color = AppTheme.colors.onSecondary,
                fontSize = 65.sp
            )
            KeyPad(
                onKeyPress = { key ->
                    when(key) {
                        '.' -> {
                            if (!actionAmount.contains('.')) {
                                actionAmount += key
                            }
                        }
                        '<' -> {
                            actionAmount = if (actionAmount.length > 1) {
                                actionAmount.substring(0, actionAmount.length - 1)
                            } else {
                                "0"
                            }
                        }
                        else -> {
                            if (key.isDigit() &&
                                (actionAmount.length < 3 ||
                                        actionAmount[actionAmount.length - 3] != '.')) {
                                if (actionAmount == "0") {
                                    actionAmount = ""
                                }
                                actionAmount += key
                            }
                        }
                    }
                }
            )
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 40.dp)
            ) {
                RequestButton(
                    onClick = {
                        navController.navigate(
                            R.id.createDebtAction,
                            Bundle().apply {
                                this.putDouble("amount", actionAmount.toDouble())
                            }
                        )
                    }
                )
                SettleButton()
            }
        }
    }

}

@Composable
fun KeyPad(
    onKeyPress: (Char) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacing),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(top = 50.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacing)
        ) {
            Button(
                onClick = { onKeyPress('1') },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppTheme.colors.secondary),
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "1",
                    fontSize = 20.sp,
                    color = AppTheme.colors.onSecondary
                )
            }
            Button(
                onClick = { onKeyPress('2') },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppTheme.colors.secondary),
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "2",
                    fontSize = 20.sp,
                    color = AppTheme.colors.onSecondary
                )
            }
            Button(
                onClick = { onKeyPress('3') },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppTheme.colors.secondary),
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "3",
                    fontSize = 20.sp,
                    color = AppTheme.colors.onSecondary
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacing)
        ) {
            Button(
                onClick = { onKeyPress('4') },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppTheme.colors.secondary),
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "4",
                    fontSize = 20.sp,
                    color = AppTheme.colors.onSecondary
                )
            }
            Button(
                onClick = { onKeyPress('5') },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppTheme.colors.secondary),
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "5",
                    fontSize = 20.sp,
                    color = AppTheme.colors.onSecondary
                )
            }
            Button(
                onClick = { onKeyPress('6') },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppTheme.colors.secondary),
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "6",
                    fontSize = 20.sp,
                    color = AppTheme.colors.onSecondary
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacing)
        ) {
            Button(
                onClick = { onKeyPress('7') },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppTheme.colors.secondary),
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "7",
                    fontSize = 20.sp,
                    color = AppTheme.colors.onSecondary
                )
            }
            Button(
                onClick = { onKeyPress('8') },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppTheme.colors.secondary),
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "8",
                    fontSize = 20.sp,
                    color = AppTheme.colors.onSecondary
                )
            }
            Button(
                onClick = { onKeyPress('9') },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppTheme.colors.secondary),
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "9",
                    fontSize = 20.sp,
                    color = AppTheme.colors.onSecondary
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacing)
        ) {
            Button(
                onClick = { onKeyPress('.') },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppTheme.colors.secondary),
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = ".",
                    fontSize = 20.sp,
                    color = AppTheme.colors.onSecondary
                )
            }
            Button(
                onClick = { onKeyPress('0') },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppTheme.colors.secondary),
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "0",
                    fontSize = 20.sp,
                    color = AppTheme.colors.onSecondary
                )
            }
            Button(
                onClick = { onKeyPress('<') },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = AppTheme.colors.secondary),
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "<",
                    fontSize = 20.sp,
                    color = AppTheme.colors.onSecondary
                )
            }
        }
    }
}

@Composable
fun RequestButton(
    onClick: () -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = AppTheme.colors.secondary),
        modifier = Modifier
            .padding(bottom = AppTheme.dimensions.paddingMedium)
            .width(150.dp)
            .height(40.dp),
        shape = AppTheme.shapes.large,
        onClick = onClick
    ) {
        Text(
            text = "Request",
            color = AppTheme.colors.onSecondary,
        )
    }
}

@Composable
fun SettleButton() {
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = AppTheme.colors.secondary),
        modifier = Modifier
            .padding(bottom = AppTheme.dimensions.paddingMedium)
            .width(150.dp)
            .height(40.dp),
        shape = AppTheme.shapes.large,
        onClick = { /*TODO*/ }
    ) {
        Text(
            text = "Settle",
            color = AppTheme.colors.onSecondary,
        )
    }
}

@Composable
fun ActionAmountTopAppBar(
    onBackPress: () -> Unit
) {
    TopAppBar(
        title = { Text("Request Amount", color = AppTheme.colors.onSecondary) },
        backgroundColor = AppTheme.colors.primary,
        navigationIcon = {
            IconButton(
                onClick = onBackPress
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        }
    )
}
