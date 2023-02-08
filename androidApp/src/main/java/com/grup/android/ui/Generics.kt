package com.grup.android.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.*
import com.grup.android.*
import com.grup.android.transaction.TransactionActivity
import com.grup.android.ui.apptheme.AppTheme
import com.grup.models.UserInfo

@Composable
fun h1Text(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = AppTheme.colors.onSecondary,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontWeight: FontWeight? = null
) {
    Text(
        text = text,
        color = color,
        style = AppTheme.typography.h1,
        fontSize = fontSize,
        fontWeight = fontWeight,
        modifier = modifier
    )
}


@Composable
fun caption(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = AppTheme.colors.onSecondary,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        style = AppTheme.typography.smallFont,
        fontSize = fontSize
    )
}

@Composable
fun SmallIcon(
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = AppTheme.colors.onPrimary,
        modifier = modifier.size(AppTheme.dimensions.iconSize)
    )
}

@Composable
fun ProfileIcon(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    contentDescription: String = "Profile Picture",
    iconSize: Dp = 70.dp
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier.size(iconSize)
    )
}

@Composable
fun SmallIconButton(
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = {  },
        modifier = Modifier
            .size(AppTheme.dimensions.borderIconSize)
            .shadow(
                elevation = AppTheme.dimensions.shadowElevationSize,
                shape = AppTheme.shapes.CircleShape,
                clip = false
            )
            .clip(AppTheme.shapes.CircleShape)
            .background(color = AppTheme.colors.caption)
            .border(
                border = BorderStroke(1.dp, AppTheme.colors.secondary),
                shape = AppTheme.shapes.CircleShape
            )
    ) {
        SmallIcon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = modifier.clip(AppTheme.shapes.CircleShape)
        )
    }
}

@Composable
fun IconRowCard(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Default.Face,
    iconSize: Dp = 70.dp,
    mainContent: @Composable () -> Unit,
    sideContent: @Composable () -> Unit = {},
    onClick: (() -> Unit)? = null
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth(0.95f)
            .let { if (onClick != null) it.clickable(onClick = onClick) else it }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacingSmall)
        ) {
            ProfileIcon(
                imageVector = icon,
                iconSize = iconSize
            )
            mainContent()
        }
        sideContent()
    }
}

@Composable
fun UserInfoRowCard(
    modifier: Modifier = Modifier,
    userInfo: UserInfo,
    mainContent: @Composable (UserInfo) -> Unit = {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxHeight(0.8f)
        ) {
            h1Text(text = it.nickname!!)
        }
    },
    sideContent: @Composable (UserInfo) -> Unit = {
        MoneyAmount(
            moneyAmount = userInfo.userBalance,
            fontSize = 24.sp
        )
    },
    iconSize: Dp = 70.dp,
    onClick: (() -> Unit)? = null
) {
    IconRowCard(
        mainContent = { mainContent(userInfo) },
        sideContent = { sideContent(userInfo) },
        iconSize = iconSize,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
fun MoneyAmount(
    moneyAmount: Double,
    fontSize: TextUnit = 30.sp
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .height(IntrinsicSize.Min)
    ) {
        h1Text(
            text = moneyAmount.asMoneyAmount().substring(0, 1),
            fontSize = fontSize.times(0.6),
            modifier = Modifier.padding(top = 4.dp)
        )
        h1Text(
            text = moneyAmount.asMoneyAmount().substring(1),
            fontSize = fontSize
        )
    }
}

@Composable
fun RecentActivityList(
    modifier: Modifier = Modifier,
    groupActivity: List<TransactionActivity>
) {
    val groupActivityByDate: Map<String, List<TransactionActivity>> =
        groupActivity.groupBy { isoDate(it.date) }
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacing),
        modifier = modifier.fillMaxWidth()
    ) {
        h1Text(text = "Recent Transactions", fontWeight = FontWeight.Medium)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(AppTheme.shapes.large)
                .background(AppTheme.colors.secondary)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimensions.spacing),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(AppTheme.dimensions.cardPadding)
            ) {
                items(groupActivityByDate.keys.toList()) { date ->
                    caption(text = date)
                    Spacer(modifier = Modifier.height(AppTheme.dimensions.spacing))
                    Column(
                        verticalArrangement = Arrangement
                            .spacedBy(AppTheme.dimensions.spacingSmall),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        groupActivityByDate[date]!!.forEach { transactionActivity ->
                            h1Text(
                                text = transactionActivity.displayText(),
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DrawerHeader(
    navigateNotificationsOnClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 20.dp)
    ) {
        Text(text = "Groups", fontSize = 40.sp, color = AppTheme.colors.onPrimary)

        Spacer(modifier = Modifier.weight(1f))

        NotificationsButton(navigateNotificationsOnClick = navigateNotificationsOnClick)
    }
}

@Composable
fun DrawerBody(
    items: List<GroupItem>,
    itemTextStyle: TextStyle = TextStyle(fontSize = 25.sp),
    onItemClick: (GroupItem) -> Unit
) {
    LazyColumn {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(20.dp)
            ) {
                SmallIcon(
                    imageVector = item.icon,
                    contentDescription = item.contentDescription
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = item.groupName,
                    style = itemTextStyle,
                    color = AppTheme.colors.onPrimary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun DrawerSettings(
    items: List<MenuItem>,
    itemTextStyle: TextStyle = TextStyle(fontSize = 15.sp),
    onItemClick: (MenuItem) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.Bottom)
    {
        items(items) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onItemClick(item)
                    }
                    .padding(13.dp)
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.contentDescription,
                    tint = AppTheme.colors.onPrimary
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    style = itemTextStyle,
                    color = AppTheme.colors.onPrimary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun UsernameSearchBar(
    modifier: Modifier = Modifier,
    usernameSearchQuery: String,
    onQueryChange: (String) -> Unit,
    border: Color = Color.Transparent
) {
    Row(modifier = modifier) {
        TextField(
            value = usernameSearchQuery,
            onValueChange = onQueryChange,
            label = { Text("Search", color = AppTheme.colors.primary) },
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "SearchIcon"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clip(AppTheme.shapes.large)
                .background(AppTheme.colors.secondary),
            colors = TextFieldDefaults.textFieldColors(
                textColor = AppTheme.colors.primary,
                disabledTextColor = Color.Transparent,
                backgroundColor = AppTheme.colors.onPrimary,
                focusedIndicatorColor = border,
                unfocusedIndicatorColor = border,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TransparentTextField(
    modifier: Modifier = Modifier,
    value: String,
    textColor: Color = AppTheme.colors.onSecondary,
    onValueChange: (String) -> Unit
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = textColor),
        singleLine = true,
        decorationBox = { innerTextField ->
            TextFieldDefaults.TextFieldDecorationBox(
                value = value,
                innerTextField = innerTextField,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource  = remember { MutableInteractionSource() },
                label = { h1Text(text = "Message") }
            )
        },
        modifier = modifier.width(IntrinsicSize.Min)
    )
}
