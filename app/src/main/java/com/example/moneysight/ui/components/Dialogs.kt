package com.moneysight.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Group
import com.moneysight.app.model.ExpenseListInfo
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import com.moneysight.app.viewmodel.CategoryBreakdownData
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.text.style.TextAlign
import com.moneysight.app.ui.theme.ChecklistPurpleContainer
import com.moneysight.app.ui.theme.ChecklistPurpleLight
import com.moneysight.app.ui.theme.ChecklistPurplePrimary
import com.moneysight.app.ui.theme.ChecklistSurfaceVariant
import com.moneysight.app.ui.theme.EntryTeal
import com.moneysight.app.ui.theme.EntryTealContainer
import com.moneysight.app.ui.theme.ExpenseRose
import com.moneysight.app.ui.theme.IncomeGreen
import com.moneysight.app.ui.theme.IncomeGreenContainer
import com.moneysight.app.ui.theme.LocalMoneySightColors
import com.moneysight.app.ui.theme.OnChecklistPurple
import com.moneysight.app.ui.theme.OnEntryTeal
import com.moneysight.app.ui.theme.ThemeMode
import com.moneysight.app.util.ExpenseCategories
import com.moneysight.app.util.ExpenseCategory
import com.moneysight.app.util.PresetCategoryItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moneysight.app.model.Expense
import com.moneysight.app.model.ExpenseGroup
import com.moneysight.app.model.IncomeSource
import com.moneysight.app.ui.theme.ForestCardBorder
import com.moneysight.app.ui.theme.ForestSurface
import com.moneysight.app.ui.theme.ForestSurfaceVariant
import com.moneysight.app.ui.theme.MintGreenDark
import com.moneysight.app.ui.theme.MintGreenPrimary
import com.moneysight.app.ui.theme.OnMintGreen
import com.moneysight.app.ui.theme.TextMutedOnDark
import com.moneysight.app.ui.theme.TextPrimaryOnDark
import com.moneysight.app.ui.theme.TextSecondaryOnDark
import com.moneysight.app.ui.theme.WarningAmber
import com.moneysight.app.ui.theme.WarningRed
import com.moneysight.app.util.CurrencyUtils

/**
 * Dialog to Manage Expense Groups / People (e.g. "Dad", "House", "Alex").
 */
@Composable
fun ManageGroupsDialog(
    groups: List<ExpenseGroup>,
    groupNameInput: String,
    onNameChanged: (String) -> Unit,
    onAddGroupClicked: () -> Unit,
    onDeleteGroupClicked: (ExpenseGroup) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForestSurface,
        shape = RoundedCornerShape(20.dp),
        icon = {
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = null,
                tint = MintGreenPrimary,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(
                text = "Expense Groups & People",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryOnDark
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Create separate group names (e.g. Dad, Mum, Alex, House Expenses) to assign and organize expenses.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondaryOnDark
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Add New Group Form
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = groupNameInput,
                        onValueChange = onNameChanged,
                        modifier = Modifier.weight(1f),
                        label = { Text("Group / Person Name", color = TextSecondaryOnDark) },
                        placeholder = { Text("e.g. Dad, Alex, House", color = TextMutedOnDark.copy(alpha = 0.6f)) },
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ForestSurfaceVariant,
                            unfocusedContainerColor = ForestSurfaceVariant,
                            focusedBorderColor = MintGreenPrimary,
                            unfocusedBorderColor = ForestCardBorder,
                            focusedTextColor = TextPrimaryOnDark,
                            unfocusedTextColor = TextPrimaryOnDark
                        )
                    )

                    Button(
                        onClick = onAddGroupClicked,
                        enabled = groupNameInput.isNotBlank(),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MintGreenPrimary,
                            contentColor = OnMintGreen
                        )
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // List of Existing Groups
                Text(
                    text = "Your Groups (${groups.size})",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryOnDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(groups, key = { it.id }) { group ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(ForestSurfaceVariant)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(MintGreenDark),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Group,
                                        contentDescription = null,
                                        tint = MintGreenPrimary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = group.name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryOnDark
                                )
                            }

                            if (group.id != "general") {
                                IconButton(
                                    onClick = { onDeleteGroupClicked(group) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DeleteOutline,
                                        contentDescription = "Delete Group",
                                        tint = WarningRed,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MintGreenPrimary,
                    contentColor = OnMintGreen
                )
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    )
}

/**
 * Delete Expense Group Confirmation Dialog.
 */
@Composable
fun DeleteGroupConfirmationDialog(
    group: ExpenseGroup,
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForestSurface,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = WarningRed,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Delete Group \"${group.name}\"?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryOnDark
                )
            }
        },
        text = {
            Text(
                text = "Are you sure you want to delete the group \"${group.name}\"? Expenses in this group will revert to 'General'.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondaryOnDark
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirmDelete,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WarningRed,
                    contentColor = TextPrimaryOnDark
                )
            ) {
                Text("Delete", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ForestCardBorder))
            ) {
                Text("Cancel", color = TextSecondaryOnDark)
            }
        }
    )
}

/**
 * Dialog to Manage Multiple Income Sources (Add, View List, Edit, Delete).
 */
@Composable
fun IncomeSourcesDialog(
    incomeSources: List<IncomeSource>,
    totalIncome: Double,
    incomeNameInput: String,
    incomeAmountInput: String,
    onNameChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onAddIncomeClicked: () -> Unit,
    onEditClicked: (IncomeSource) -> Unit,
    onDeleteClicked: (IncomeSource) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForestSurface,
        shape = RoundedCornerShape(20.dp),
        icon = {
            Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = MintGreenPrimary,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Income Sources",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryOnDark
                )
                Text(
                    text = "Total Income: ${CurrencyUtils.formatPounds(totalIncome)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MintGreenPrimary
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Add Income Source Form
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ForestSurfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Text(
                            text = "Add New Income Source",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryOnDark
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = incomeNameInput,
                            onValueChange = onNameChanged,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Source Name", color = TextSecondaryOnDark) },
                            placeholder = { Text("e.g. Salary, Side Hustle, Bonus", color = TextMutedOnDark.copy(alpha = 0.6f)) },
                            singleLine = true,
                            shape = RoundedCornerShape(10.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ForestSurface,
                                unfocusedContainerColor = ForestSurface,
                                focusedBorderColor = MintGreenPrimary,
                                unfocusedBorderColor = ForestCardBorder,
                                focusedTextColor = TextPrimaryOnDark,
                                unfocusedTextColor = TextPrimaryOnDark
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = incomeAmountInput,
                                onValueChange = { input ->
                                    val clean = input.replace(",", ".")
                                    if (clean.isEmpty() || clean.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                                        onAmountChanged(clean)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                label = { Text("Amount (£)", color = TextSecondaryOnDark) },
                                prefix = { Text("£ ", fontWeight = FontWeight.Bold, color = MintGreenPrimary) },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = ForestSurface,
                                    unfocusedContainerColor = ForestSurface,
                                    focusedBorderColor = MintGreenPrimary,
                                    unfocusedBorderColor = ForestCardBorder,
                                    focusedTextColor = TextPrimaryOnDark,
                                    unfocusedTextColor = TextPrimaryOnDark
                                )
                            )
                            val isValid = incomeAmountInput.toDoubleOrNull()?.let { it >= 0 } == true
                            Button(
                                onClick = onAddIncomeClicked,
                                enabled = isValid,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.height(52.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MintGreenPrimary,
                                    contentColor = OnMintGreen
                                )
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Add", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // List of Existing Income Sources
                if (incomeSources.isEmpty()) {
                    Text(
                        text = "No income sources added yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondaryOnDark,
                        fontSize = 13.sp
                    )
                } else {
                    Text(
                        text = "Your Income Sources (${incomeSources.size})",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryOnDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(incomeSources, key = { it.id }) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(ForestSurfaceVariant)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryOnDark
                                    )
                                    Text(
                                        text = CurrencyUtils.formatPounds(item.amount),
                                        style = MaterialTheme.typography.bodySmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MintGreenPrimary
                                    )
                                }
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MintGreenDark)
                                            .clickable { onEditClicked(item) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = MintGreenPrimary,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(WarningRed.copy(alpha = 0.2f))
                                            .clickable { onDeleteClicked(item) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DeleteOutline,
                                            contentDescription = "Delete",
                                            tint = WarningRed,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MintGreenPrimary,
                    contentColor = OnMintGreen
                )
            ) {
                Text("Done", fontWeight = FontWeight.Bold)
            }
        }
    )
}

/**
 * Edit Income Source Dialog.
 */
@Composable
fun EditIncomeSourceDialog(
    nameInput: String,
    amountInput: String,
    onNameChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForestSurface,
        shape = RoundedCornerShape(20.dp),
        icon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = MintGreenPrimary,
                modifier = Modifier.size(28.dp)
            )
        },
        title = {
            Text(
                text = "Edit Income Source",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryOnDark
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = onNameChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Income Source Name", color = TextSecondaryOnDark) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ForestSurfaceVariant,
                        unfocusedContainerColor = ForestSurfaceVariant,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = ForestCardBorder,
                        focusedTextColor = TextPrimaryOnDark,
                        unfocusedTextColor = TextPrimaryOnDark
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = amountInput,
                    onValueChange = { input ->
                        val clean = input.replace(",", ".")
                        if (clean.isEmpty() || clean.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                            onAmountChanged(clean)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Amount (£)", color = TextSecondaryOnDark) },
                    prefix = { Text("£ ", fontWeight = FontWeight.Bold, color = MintGreenPrimary) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ForestSurfaceVariant,
                        unfocusedContainerColor = ForestSurfaceVariant,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = ForestCardBorder,
                        focusedTextColor = TextPrimaryOnDark,
                        unfocusedTextColor = TextPrimaryOnDark
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = nameInput.isNotBlank() && amountInput.toDoubleOrNull()?.let { it >= 0 } == true,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MintGreenPrimary,
                    contentColor = OnMintGreen
                )
            ) {
                Text("Save Changes", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ForestCardBorder))
            ) {
                Text("Cancel", color = TextSecondaryOnDark)
            }
        }
    )
}

/**
 * Delete Income Source Confirmation Dialog.
 */
@Composable
fun DeleteIncomeConfirmationDialog(
    incomeSource: IncomeSource,
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForestSurface,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = WarningRed,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Delete Income Source?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryOnDark
                )
            }
        },
        text = {
            Text(
                text = "Are you sure you want to delete \"${incomeSource.name}\" (${CurrencyUtils.formatPounds(incomeSource.amount)})?",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondaryOnDark
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirmDelete,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WarningRed,
                    contentColor = TextPrimaryOnDark
                )
            ) {
                Text("Delete", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ForestCardBorder))
            ) {
                Text("Cancel", color = TextSecondaryOnDark)
            }
        }
    )
}

/**
 * Warning Modal when an expense exceeds remaining balance.
 */
@Composable
fun OverbudgetWarningDialog(
    expenseAmount: Double,
    remainingBalance: Double,
    onConfirmProceed: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForestSurface,
        shape = RoundedCornerShape(20.dp),
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                tint = WarningAmber,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                text = "Expense Exceeds Balance",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = WarningAmber
            )
        },
        text = {
            Column {
                Text(
                    text = "This expense of ${CurrencyUtils.formatPounds(expenseAmount)} is greater than your available remaining balance of ${CurrencyUtils.formatPounds(remainingBalance)}.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimaryOnDark
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Do you still want to add this expense?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondaryOnDark,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmProceed,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WarningAmber,
                    contentColor = OnMintGreen
                )
            ) {
                Text("Proceed Anyway", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ForestCardBorder))
            ) {
                Text("Cancel", color = TextSecondaryOnDark)
            }
        }
    )
}

/**
 * Edit Expense Dialog with 2 top interactive boxes: Category & Group (opening pop-up pickers when clicked).
 */
@Composable
fun EditExpenseDialog(
    descriptionInput: String,
    amountInput: String,
    notesInput: String = "",
    dueDateInput: String = "",
    selectedCategory: String = "General",
    selectedGroupId: String,
    groups: List<ExpenseGroup>,
    selectedPresets: Set<String> = emptySet(),
    customPresets: List<String> = emptyList(),
    onDescriptionChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onNotesChanged: (String) -> Unit = {},
    onDueDateChanged: (String) -> Unit = {},
    onCategoryChanged: (String) -> Unit = {},
    onGroupIdChanged: (String) -> Unit,
    onConfirm: () -> Unit,
    onDelete: (() -> Unit)? = null,
    onDismiss: () -> Unit
) {
    var showCategoryPopup by remember { mutableStateOf(false) }
    var showGroupPopup by remember { mutableStateOf(false) }

    val availableCategoryPresets = remember(selectedPresets, customPresets) {
        val builtIn = ExpenseCategories.categoryGridPresets.filter {
            selectedPresets.isEmpty() || selectedPresets.contains(it.title)
        }
        val customItems = customPresets.filter { selectedPresets.contains(it) }.map { customTitle ->
            ExpenseCategories.getPresetForCategory(customTitle)
        }
        val combined = (builtIn + customItems).distinctBy { it.title }
        if (combined.isEmpty()) ExpenseCategories.categoryGridPresets else combined
    }

    val selectedGroup = remember(selectedGroupId, groups) {
        groups.find { it.id == selectedGroupId } ?: ExpenseGroup.DEFAULT_GENERAL
    }

    val currentCategoryTitle = remember(descriptionInput, selectedCategory) {
        if (selectedCategory.isNotBlank() && !selectedCategory.equals("General", ignoreCase = true)) {
            selectedCategory
        } else {
            ExpenseCategories.getCategoryForExpense(descriptionInput)
        }
    }

    val selectedPreset = remember(currentCategoryTitle) {
        ExpenseCategories.getPresetForCategory(currentCategoryTitle)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForestSurface,
        shape = RoundedCornerShape(24.dp),
        icon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null,
                tint = MintGreenPrimary,
                modifier = Modifier.size(28.dp)
            )
        },
        title = {
            Text(
                text = "Edit Expense",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryOnDark
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 2 Boxes on Top: Category Box & Group Box
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Box 1: Category Box
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(ForestSurfaceVariant)
                            .clickable { showCategoryPopup = true }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Category",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondaryOnDark,
                                    fontSize = 11.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = TextPrimaryOnDark,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clip(CircleShape)
                                        .background(selectedPreset.color),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = ExpenseCategories.getCategoryIcon(selectedPreset.iconName),
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(11.dp)
                                    )
                                }

                                Text(
                                    text = selectedPreset.title,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryOnDark,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    // Box 2: Group Box
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(ForestSurfaceVariant)
                            .clickable { showGroupPopup = true }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Group",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondaryOnDark,
                                    fontSize = 11.sp
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = null,
                                    tint = TextPrimaryOnDark,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Groups,
                                    contentDescription = null,
                                    tint = MintGreenPrimary,
                                    modifier = Modifier.size(18.dp)
                                )

                                Text(
                                    text = selectedGroup.name,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryOnDark,
                                    fontSize = 13.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = descriptionInput,
                    onValueChange = onDescriptionChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Description / Bill Name", color = TextSecondaryOnDark) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ForestSurfaceVariant,
                        unfocusedContainerColor = ForestSurfaceVariant,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = ForestCardBorder,
                        focusedTextColor = TextPrimaryOnDark,
                        unfocusedTextColor = TextPrimaryOnDark,
                        cursorColor = MintGreenPrimary
                    )
                )

                OutlinedTextField(
                    value = amountInput,
                    onValueChange = { input ->
                        val clean = input.replace(",", ".")
                        if (clean.isEmpty() || clean.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                            onAmountChanged(clean)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Amount (£)", color = TextSecondaryOnDark) },
                    prefix = { Text("£ ", fontWeight = FontWeight.Bold, color = MintGreenPrimary) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ForestSurfaceVariant,
                        unfocusedContainerColor = ForestSurfaceVariant,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = ForestCardBorder,
                        focusedTextColor = TextPrimaryOnDark,
                        unfocusedTextColor = TextPrimaryOnDark,
                        cursorColor = MintGreenPrimary
                    )
                )

                OutlinedTextField(
                    value = dueDateInput,
                    onValueChange = onDueDateChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Date bill is coming out (optional)", color = TextSecondaryOnDark) },
                    placeholder = { Text("e.g. 1st, 15th, 28th", color = TextMutedOnDark.copy(alpha = 0.5f), fontSize = 12.sp) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ForestSurfaceVariant,
                        unfocusedContainerColor = ForestSurfaceVariant,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = ForestCardBorder,
                        focusedTextColor = TextPrimaryOnDark,
                        unfocusedTextColor = TextPrimaryOnDark,
                        cursorColor = MintGreenPrimary
                    )
                )

                OutlinedTextField(
                    value = notesInput,
                    onValueChange = onNotesChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Notes (optional)", color = TextSecondaryOnDark) },
                    placeholder = { Text("Add any notes or details...", color = TextMutedOnDark.copy(alpha = 0.5f), fontSize = 12.sp) },
                    singleLine = false,
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = ForestSurfaceVariant,
                        unfocusedContainerColor = ForestSurfaceVariant,
                        focusedBorderColor = MintGreenPrimary,
                        unfocusedBorderColor = ForestCardBorder,
                        focusedTextColor = TextPrimaryOnDark,
                        unfocusedTextColor = TextPrimaryOnDark,
                        cursorColor = MintGreenPrimary
                    )
                )
            }
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Primary Action: Full-Width Save Changes Button
                Button(
                    onClick = onConfirm,
                    enabled = descriptionInput.isNotBlank() && amountInput.toDoubleOrNull()?.let { it > 0 } == true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MintGreenPrimary,
                        contentColor = OnMintGreen
                    )
                ) {
                    Text("Save Changes", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                // Secondary Actions: Centered Side-by-Side Delete & Cancel Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (onDelete != null) {
                        Button(
                            onClick = onDelete,
                            modifier = Modifier
                                .weight(1f)
                                .height(42.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = WarningRed,
                                contentColor = TextPrimaryOnDark
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = "Delete",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Delete", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ForestCardBorder))
                    ) {
                        Text("Cancel", color = TextSecondaryOnDark, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        },
        dismissButton = null
    )

    // Category Selector Pop-up
    if (showCategoryPopup) {
        AlertDialog(
            onDismissRequest = { showCategoryPopup = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(28.dp),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.GridView,
                        contentDescription = null,
                        tint = Color(0xFF00796B),
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Select Category",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF002920)
                    )
                }
            },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 380.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(availableCategoryPresets, key = { it.title }) { preset ->
                        val isSelected = selectedPreset.title.equals(preset.title, ignoreCase = true)

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) Color(0xFFD6F2EA) else Color.Transparent)
                                .clickable {
                                    onCategoryChanged(preset.title)
                                    showCategoryPopup = false
                                }
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(CircleShape)
                                            .background(preset.color),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = ExpenseCategories.getCategoryIcon(preset.iconName),
                                            contentDescription = preset.title,
                                            tint = Color.White,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }

                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .align(Alignment.BottomEnd)
                                                .clip(CircleShape)
                                                .background(Color(0xFF00796B)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = preset.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F2D2A),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showCategoryPopup = false },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00897B),
                        contentColor = Color.White
                    )
                ) {
                    Text("Close", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Group Selector Pop-up
    if (showGroupPopup) {
        AlertDialog(
            onDismissRequest = { showGroupPopup = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Groups,
                        contentDescription = null,
                        tint = Color(0xFF00796B),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Assign to Group",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF002920)
                    )
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(groups, key = { it.id }) { group ->
                        val isSelected = selectedGroupId == group.id
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFFD6F2EA) else Color(0xFFE2ECE8))
                                .clickable {
                                    onGroupIdChanged(group.id)
                                    showGroupPopup = false
                                }
                                .padding(horizontal = 14.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = group.name,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = Color(0xFF0B2822)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showGroupPopup = false },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00897B),
                        contentColor = Color.White
                    )
                ) {
                    Text("Close", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

/**
 * Delete Expense Confirmation Dialog.
 */
@Composable
fun DeleteConfirmationDialog(
    expense: Expense,
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForestSurface,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = WarningRed,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Delete Expense?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryOnDark
                )
            }
        },
        text = {
            Text(
                text = "Are you sure you want to delete \"${expense.description}\" (${CurrencyUtils.formatPounds(expense.amount)})? This will restore ${CurrencyUtils.formatPounds(expense.amount)} back to your remaining balance.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondaryOnDark
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirmDelete,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WarningRed,
                    contentColor = TextPrimaryOnDark
                )
            ) {
                Text("Delete", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ForestCardBorder))
            ) {
                Text("Cancel", color = TextSecondaryOnDark)
            }
        }
    )
}

/**
 * Settings & Preferences Hub (Grouped Cards for Theme, Monthly Stamp Reset, and Presets Manager).
 */
@Composable
fun SettingsDialog(
    currentThemeMode: ThemeMode,
    combinedCategories: List<ExpenseCategory>,
    selectedPresets: Set<String>,
    autoResetStampEnabled: Boolean = false,
    autoResetStampDay: Int = 1,
    onToggleThemeClicked: () -> Unit,
    onOpenResetAllStampsClicked: () -> Unit = {},
    onToggleAutoResetStampEnabled: () -> Unit = {},
    onSetAutoResetStampDay: (Int) -> Unit = {},
    onOpenManagePresetsClicked: () -> Unit = {},
    onDismiss: () -> Unit
) {
    val totalAvailable = combinedCategories.sumOf { it.items.size }
    val totalSelected = selectedPresets.size
    val isSystemDark = androidx.compose.foundation.isSystemInDarkTheme()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForestSurface,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = EntryTeal,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Settings & Preferences",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryOnDark
                    )
                    Text(
                        text = "Customize your app preferences",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondaryOnDark,
                        fontSize = 11.sp
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. Theme Mode Switcher Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ForestSurfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onToggleThemeClicked() }
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val (themeIcon, themeLabel) = when (currentThemeMode) {
                                ThemeMode.DARK -> Icons.Default.DarkMode to "Dark Mode"
                                ThemeMode.LIGHT -> Icons.Default.WbSunny to "Light Mode"
                                ThemeMode.SYSTEM -> Icons.Default.SettingsBrightness to if (isSystemDark) "Auto (Dark)" else "Auto (Light)"
                            }
                            Icon(
                                imageVector = themeIcon,
                                contentDescription = "Theme Mode",
                                tint = MintGreenPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Theme Mode",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryOnDark,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = themeLabel,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MintGreenPrimary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(EntryTealContainer)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Switch Theme",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = EntryTeal,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                // 2. Monthly Stamp Reset & Auto-Reset Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ForestSurfaceVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Monthly Stamp Reset",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryOnDark,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "Clear main page stamps for a new month",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondaryOnDark,
                                    fontSize = 11.sp
                                )
                            }

                            Button(
                                onClick = onOpenResetAllStampsClicked,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = IncomeGreen,
                                    contentColor = TextPrimaryOnDark
                                ),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("Reset Now", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }

                        // Auto-Reset Toggle Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Auto-Reset Monthly at 00:00",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryOnDark,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = if (autoResetStampEnabled) "Automatically clears stamps on Day $autoResetStampDay at 00:00" else "Off",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (autoResetStampEnabled) IncomeGreen else TextSecondaryOnDark,
                                    fontSize = 11.sp
                                )
                            }

                            Switch(
                                checked = autoResetStampEnabled,
                                onCheckedChange = { onToggleAutoResetStampEnabled() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = TextPrimaryOnDark,
                                    checkedTrackColor = IncomeGreen,
                                    uncheckedThumbColor = TextSecondaryOnDark,
                                    uncheckedTrackColor = ForestCardBorder
                                )
                            )
                        }

                        // Day of Month Selector when Auto-Reset Enabled
                        if (autoResetStampEnabled) {
                            Column {
                                Text(
                                    text = "Auto-Reset Day of Month:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondaryOnDark,
                                    fontSize = 10.sp
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    listOf(1, 15, 28).forEach { day ->
                                        val isSelected = autoResetStampDay == day
                                        val dayLabel = when (day) {
                                            1 -> "1st"
                                            15 -> "15th"
                                            else -> "28th"
                                        }
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(14.dp))
                                                .background(if (isSelected) IncomeGreen else ForestSurface)
                                                .clickable { onSetAutoResetStampDay(day) }
                                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = dayLabel,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) TextPrimaryOnDark else TextSecondaryOnDark,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // 3. Preset Expenses Manager Card (Clicking opens dedicated spacious Presets Manager!)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ForestSurfaceVariant)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onOpenManagePresetsClicked() }
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.GridView,
                                contentDescription = "Manage Presets",
                                tint = EntryTeal,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Preset Expenses Manager",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimaryOnDark,
                                    fontSize = 13.sp
                                )
                                Text(
                                    text = "$totalSelected / $totalAvailable presets active",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = EntryTeal,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(EntryTealContainer)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Manage",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = EntryTeal,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EntryTeal,
                    contentColor = OnEntryTeal
                )
            ) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        }
    )
}

/**
 * Dialog to Manage Expense Presets (Selecting 42 built-in visual category presets & custom extra presets).
 */
@Composable
fun ManagePresetsDialog(
    customPresets: List<String>,
    selectedPresets: Set<String>,
    customPresetInput: String,
    onCustomPresetInputChanged: (String) -> Unit,
    onAddCustomPresetClicked: () -> Unit,
    onRemoveCustomPresetClicked: (String) -> Unit,
    onTogglePresetSelected: (String) -> Unit,
    onSelectAllClicked: () -> Unit,
    onDeselectAllClicked: () -> Unit,
    onDismiss: () -> Unit
) {
    val totalAvailable = ExpenseCategories.categoryGridPresets.size + customPresets.size
    val totalSelected = selectedPresets.size

    val allPresetItems = remember(customPresets) {
        val builtIn = ExpenseCategories.categoryGridPresets
        val customItems = customPresets.map { customTitle ->
            ExpenseCategories.getPresetForCategory(customTitle)
        }
        (builtIn + customItems).distinctBy { it.title }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(28.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.GridView,
                    contentDescription = null,
                    tint = Color(0xFF00796B),
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Manage Presets",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF002920)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Select presets to show in 'Where is your money going?'",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF4B635C),
                        fontSize = 11.5.sp
                    )
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Add Custom Extra Preset Input Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = customPresetInput,
                        onValueChange = onCustomPresetInputChanged,
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                "Add custom preset...",
                                color = Color(0xFF6A877E).copy(alpha = 0.8f),
                                fontSize = 13.sp
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { onAddCustomPresetClicked() }),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFE2F0EC),
                            unfocusedContainerColor = Color(0xFFE2F0EC),
                            focusedBorderColor = Color(0xFF00796B),
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color(0xFF002920),
                            unfocusedTextColor = Color(0xFF002920),
                            cursorColor = Color(0xFF00796B)
                        )
                    )

                    Button(
                        onClick = onAddCustomPresetClicked,
                        enabled = customPresetInput.isNotBlank(),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFC7E5DC),
                            contentColor = Color(0xFF004D40),
                            disabledContainerColor = Color(0xFFE2F0EC),
                            disabledContentColor = Color(0xFF6A877E)
                        ),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Text("+ Extra", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Control Row: Count / Select All / Deselect All
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$totalSelected / $totalAvailable selected",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00796B),
                        fontSize = 12.sp
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            text = "Select All",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00796B),
                            fontSize = 12.sp,
                            modifier = Modifier.clickable { onSelectAllClicked() }
                        )

                        Text(
                            text = "Deselect All",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4B635C),
                            fontSize = 12.sp,
                            modifier = Modifier.clickable { onDeselectAllClicked() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 4-Column Grid of 42 Category Circle Presets
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 440.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(allPresetItems, key = { it.title }) { preset ->
                        val isSelected = selectedPresets.contains(preset.title)
                        val isCustom = customPresets.any { it.equals(preset.title, ignoreCase = true) }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) Color(0xFFD6F2EA) else Color.Transparent)
                                .clickable { onTogglePresetSelected(preset.title) }
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center
                                ) {
                                    // Main Vibrant Circular Icon
                                    Box(
                                        modifier = Modifier
                                            .size(48.dp)
                                            .clip(CircleShape)
                                            .background(preset.color),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = ExpenseCategories.getCategoryIcon(preset.iconName),
                                            contentDescription = preset.title,
                                            tint = Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    // Small Checkmark Badge overlay at bottom right when selected
                                    if (isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .align(Alignment.BottomEnd)
                                                .clip(CircleShape)
                                                .background(Color(0xFF00796B)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = preset.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1F2D2A),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 11.sp
                                )

                                if (isCustom) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Delete",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = WarningRed,
                                        fontSize = 9.sp,
                                        modifier = Modifier.clickable { onRemoveCustomPresetClicked(preset.title) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00897B),
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 10.dp)
            ) {
                Text("Done", fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    )
}

/**
 * Dialog to Select a Bill to Edit.
 */
@Composable
fun SelectBillToEditDialog(
    expenses: List<Expense>,
    onExpenseSelected: (Expense) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForestSurface,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = MintGreenPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Select Bill to Edit",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryOnDark
                )
            }
        },
        text = {
            if (expenses.isEmpty()) {
                Text(
                    text = "No expenses recorded yet. Add an expense first.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondaryOnDark
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 320.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(expenses, key = { it.id }) { expense ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onExpenseSelected(expense) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = ForestSurfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = expense.description,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryOnDark
                                    )
                                    Text(
                                        text = "Group: ${expense.groupName}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondaryOnDark,
                                        fontSize = 11.sp
                                    )
                                }
                                Text(
                                    text = "-${CurrencyUtils.formatPounds(expense.amount)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MintGreenPrimary
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MintGreenPrimary,
                    contentColor = TextPrimaryOnDark
                )
            ) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        }
    )
}

/**
 * Confirmation Dialog for resetting all main page stamps to unstamped.
 */
@Composable
fun ResetAllStampsConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForestSurface,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Verified,
                    contentDescription = null,
                    tint = IncomeGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Reset All Stamps?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryOnDark
                )
            }
        },
        text = {
            Text(
                text = "Are you sure you want to reset all main page stamps? This will unmark all paid stamps so you can start a fresh new month.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondaryOnDark
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = IncomeGreen,
                    contentColor = TextPrimaryOnDark
                )
            ) {
                Text("Reset All Stamps", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ForestCardBorder))
            ) {
                Text("Cancel", color = TextSecondaryOnDark)
            }
        }
    )
}

/**
 * Visual "Where Your Money Goes" Analytics Modal Dialog for Preview / Simulate Screen.
 * Displays interactive 3-tab graphs: Pie Chart (Donut), Bar Graph, and Breakdown list.
 */
@Composable
fun SimulateGraphDialog(
    categoryBreakdowns: List<CategoryBreakdownData>,
    totalExpenses: Double,
    allExpenses: List<Expense> = emptyList(),
    onDismiss: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Pie Chart, 1: Bar Graph, 2: Breakdown
    var selectedGraphCategory by remember { mutableStateOf<CategoryBreakdownData?>(null) }
    var showCategoryBillsModal by remember { mutableStateOf(false) }

    val isDarkGraph = LocalMoneySightColors.current.isDark
    val dialogBg = ForestSurface
    val cardBg = ForestSurfaceVariant
    val textPrimary = TextPrimaryOnDark
    val textSecondary = TextSecondaryOnDark
    val pillBg = if (isDarkGraph) ForestSurface else Color.White

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = dialogBg,
        shape = RoundedCornerShape(28.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(EntryTealContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PieChart,
                        contentDescription = null,
                        tint = EntryTeal,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Where Your Money Goes",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Category breakdown synced with live expenses",
                        style = MaterialTheme.typography.labelSmall,
                        color = textSecondary,
                        fontSize = 11.5.sp
                    )
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Total Expenses Summary Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBg)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TOTAL EXPENSES",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = EntryTeal,
                                fontSize = 10.sp,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "-${CurrencyUtils.formatPounds(totalExpenses)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = textPrimary,
                                fontSize = 20.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(pillBg)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${categoryBreakdowns.size} Categories",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = EntryTeal,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 3 Segmented Toggle Tabs (Pie Chart | Bar Graph | Breakdown)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(cardBg)
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Tab 1: Pie Chart
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selectedTab == 0) Color(0xFF6C5CE7) else Color.Transparent)
                            .clickable { selectedTab = 0 }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PieChart,
                                contentDescription = "Pie Chart",
                                tint = if (selectedTab == 0) Color.White else textSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Pie Chart",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (selectedTab == 0) Color.White else textSecondary
                            )
                        }
                    }

                    // Tab 2: Bar Graph
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selectedTab == 1) Color(0xFF6C5CE7) else Color.Transparent)
                            .clickable { selectedTab = 1 }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = "Bar Graph",
                                tint = if (selectedTab == 1) Color.White else textSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Bar Graph",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (selectedTab == 1) Color.White else textSecondary
                            )
                        }
                    }

                    // Tab 3: Breakdown
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selectedTab == 2) Color(0xFF6C5CE7) else Color.Transparent)
                            .clickable { selectedTab = 2 }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ListAlt,
                                contentDescription = "Breakdown",
                                tint = if (selectedTab == 2) Color.White else textSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Breakdown",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = if (selectedTab == 2) Color.White else textSecondary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(270.dp)
                ) {
                    if (categoryBreakdowns.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No expenses recorded yet",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F1F24)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Add expenses on the main screen to see where your money goes.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF6E6D7A),
                                fontSize = 12.5.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        when (selectedTab) {
                            0 -> {
                                // Tab 1: Donut Pie Chart Canvas
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(200.dp)
                                            .padding(6.dp)
                                            .pointerInput(categoryBreakdowns) {
                                                detectTapGestures { offset ->
                                                    val centerX = size.width / 2f
                                                    val centerY = size.height / 2f
                                                    val dx = offset.x - centerX
                                                    val dy = offset.y - centerY
                                                    val distance = kotlin.math.sqrt(dx * dx + dy * dy)

                                                    if (distance >= 25.dp.toPx() && distance <= 100.dp.toPx()) {
                                                        var angle = Math.toDegrees(kotlin.math.atan2(dy.toDouble(), dx.toDouble())).toFloat()
                                                        angle = (angle + 90f + 360f) % 360f

                                                        var accumulatedAngle = 0f
                                                        var tappedItem: CategoryBreakdownData? = null
                                                        for (item in categoryBreakdowns) {
                                                            val sweep = (item.percentage / 100f) * 360f
                                                            if (angle >= accumulatedAngle && angle < accumulatedAngle + sweep) {
                                                                tappedItem = item
                                                                break
                                                            }
                                                            accumulatedAngle += sweep
                                                        }

                                                        selectedGraphCategory = if (selectedGraphCategory?.categoryName == tappedItem?.categoryName) null else tappedItem
                                                    } else {
                                                        selectedGraphCategory = null
                                                    }
                                                }
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Canvas(modifier = Modifier.fillMaxSize()) {
                                            var startAngle = -90f
                                            categoryBreakdowns.forEach { item ->
                                                val sweepAngle = (item.percentage / 100f) * 360f
                                                val preset = ExpenseCategories.getPresetForCategory(item.categoryName)
                                                val isSelected = selectedGraphCategory?.categoryName == item.categoryName
                                                val isAnySelected = selectedGraphCategory != null

                                                val sliceColor = if (!isAnySelected || isSelected) {
                                                    preset.color
                                                } else {
                                                    preset.color.copy(alpha = 0.35f)
                                                }

                                                val strokeWidth = if (isSelected) 40.dp.toPx() else 32.dp.toPx()

                                                drawArc(
                                                    color = sliceColor,
                                                    startAngle = startAngle,
                                                    sweepAngle = sweepAngle,
                                                    useCenter = false,
                                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Butt)
                                                )
                                                startAngle += sweepAngle
                                            }
                                        }

                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                            modifier = Modifier.clickable { selectedGraphCategory = null }
                                        ) {
                                            if (selectedGraphCategory != null) {
                                                val active = selectedGraphCategory!!
                                                val preset = ExpenseCategories.getPresetForCategory(active.categoryName)

                                                Text(
                                                    text = active.categoryName.uppercase(),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = preset.color,
                                                    fontSize = 10.sp,
                                                    maxLines = 1
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "-${CurrencyUtils.formatPounds(active.totalAmount)}",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = textPrimary,
                                                    fontSize = 16.sp
                                                )
                                                Text(
                                                    text = String.format(java.util.Locale.UK, "%.1f%% • %d bill%s", active.percentage, active.billCount, if (active.billCount != 1) "s" else ""),
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = preset.color,
                                                    fontSize = 9.5.sp
                                                )
                                            } else {
                                                Text(
                                                    text = "TOTAL SPENT",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = textSecondary,
                                                    fontSize = 9.5.sp
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "-${CurrencyUtils.formatPounds(totalExpenses)}",
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = textPrimary,
                                                    fontSize = 16.sp
                                                )
                                                Text(
                                                    text = "Tap a slice for details",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = EntryTeal,
                                                    fontSize = 9.5.sp
                                                )
                                            }
                                        }

                                        // Eye Icon Button on Top Right of Graph Box when a category is selected!
                                        if (selectedGraphCategory != null) {
                                            IconButton(
                                                onClick = { showCategoryBillsModal = true },
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .offset(x = 6.dp, y = (-6).dp)
                                                    .size(36.dp)
                                                    .clip(CircleShape)
                                                    .background(EntryTealContainer)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Visibility,
                                                    contentDescription = "View Bills in Category",
                                                    tint = EntryTeal,
                                                    modifier = Modifier.size(20.dp)
                                                )
                                            }
                                        }
                                    }

                                    // Scrollable Category Chips below Donut Chart
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .horizontalScroll(rememberScrollState()),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        categoryBreakdowns.forEach { item ->
                                            val preset = ExpenseCategories.getPresetForCategory(item.categoryName)
                                            val isSelected = selectedGraphCategory?.categoryName == item.categoryName

                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(16.dp))
                                                    .background(if (isSelected) preset.color.copy(alpha = 0.22f) else cardBg)
                                                    .border(
                                                        width = if (isSelected) 1.5.dp else 0.dp,
                                                        color = if (isSelected) preset.color else Color.Transparent,
                                                        shape = RoundedCornerShape(16.dp)
                                                    )
                                                    .clickable {
                                                        selectedGraphCategory = if (isSelected) null else item
                                                    }
                                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(18.dp)
                                                            .clip(CircleShape)
                                                            .background(preset.color),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Icon(
                                                            imageVector = ExpenseCategories.getCategoryIcon(preset.iconName),
                                                            contentDescription = null,
                                                            tint = Color.White,
                                                            modifier = Modifier.size(11.dp)
                                                        )
                                                    }
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = item.categoryName,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (isSelected) preset.color else textPrimary,
                                                        fontSize = 12.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            1 -> {
                                // Tab 2: Bar Graph (Shows ALL live categories with horizontal scroll)
                                Card(
                                    modifier = Modifier.fillMaxSize(),
                                    shape = RoundedCornerShape(20.dp),
                                    colors = CardDefaults.cardColors(containerColor = cardBg)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        val maxAmount = remember(categoryBreakdowns) {
                                            categoryBreakdowns.maxOfOrNull { it.totalAmount } ?: 1.0
                                        }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(220.dp)
                                                .horizontalScroll(rememberScrollState()),
                                            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                                            verticalAlignment = Alignment.Bottom
                                        ) {
                                            categoryBreakdowns.forEach { item ->
                                                val preset = ExpenseCategories.getPresetForCategory(item.categoryName)
                                                val barFraction = (item.totalAmount / maxAmount).toFloat().coerceIn(0.12f, 1f)

                                                Column(
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.Bottom,
                                                    modifier = Modifier.width(52.dp)
                                                ) {
                                                    // Pill Badge Amount Above Bar
                                                    Box(
                                                        modifier = Modifier
                                                            .clip(RoundedCornerShape(8.dp))
                                                            .background(pillBg)
                                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                                    ) {
                                                        Text(
                                                            text = "£${item.totalAmount.toInt()}",
                                                            fontWeight = FontWeight.Bold,
                                                            color = preset.color,
                                                            fontSize = 10.sp,
                                                            maxLines = 1
                                                        )
                                                    }

                                                    Spacer(modifier = Modifier.height(6.dp))

                                                    // Vertical Bar
                                                    Box(
                                                        modifier = Modifier
                                                            .width(32.dp)
                                                            .fillMaxHeight(fraction = barFraction * 0.72f)
                                                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                                                            .background(preset.color)
                                                    )

                                                    Spacer(modifier = Modifier.height(8.dp))

                                                    // Category Icon Below Bar
                                                    Box(
                                                        modifier = Modifier
                                                            .size(32.dp)
                                                            .clip(CircleShape)
                                                            .background(preset.color),
                                                        contentAlignment = Alignment.Center
                                                    ) {
                                                        Icon(
                                                            imageVector = ExpenseCategories.getCategoryIcon(preset.iconName),
                                                            contentDescription = item.categoryName,
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

                            2 -> {
                                // Tab 3: Breakdown List
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    items(categoryBreakdowns, key = { it.categoryName }) { item ->
                                        val preset = ExpenseCategories.getPresetForCategory(item.categoryName)

                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(16.dp),
                                            colors = CardDefaults.cardColors(containerColor = cardBg)
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Box(
                                                            modifier = Modifier
                                                                .size(28.dp)
                                                                .clip(CircleShape)
                                                                .background(preset.color),
                                                            contentAlignment = Alignment.Center
                                                        ) {
                                                            Icon(
                                                                imageVector = ExpenseCategories.getCategoryIcon(preset.iconName),
                                                                contentDescription = null,
                                                                tint = Color.White,
                                                                modifier = Modifier.size(16.dp)
                                                            )
                                                        }
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text(
                                                            text = item.categoryName,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            fontWeight = FontWeight.Bold,
                                                            color = textPrimary,
                                                            fontSize = 13.5.sp
                                                        )
                                                        Spacer(modifier = Modifier.width(4.dp))
                                                        Text(
                                                            text = "(${item.billCount})",
                                                            style = MaterialTheme.typography.labelSmall,
                                                            color = textSecondary,
                                                            fontSize = 11.sp
                                                        )
                                                    }

                                                    Column(horizontalAlignment = Alignment.End) {
                                                        Text(
                                                            text = "-${CurrencyUtils.formatPounds(item.totalAmount)}",
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            fontWeight = FontWeight.Bold,
                                                            color = textPrimary,
                                                            fontSize = 13.5.sp
                                                        )
                                                        Text(
                                                            text = String.format(java.util.Locale.UK, "%.1f%%", item.percentage),
                                                            style = MaterialTheme.typography.labelSmall,
                                                            fontWeight = FontWeight.Bold,
                                                            color = preset.color,
                                                            fontSize = 11.sp
                                                        )
                                                    }
                                                }

                                                Spacer(modifier = Modifier.height(8.dp))

                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(7.dp)
                                                        .clip(RoundedCornerShape(4.dp))
                                                        .background(if (isDarkGraph) ForestSurface else Color(0xFFE0ECE6))
                                                ) {
                                                    Box(
                                                        modifier = Modifier
                                                            .fillMaxWidth(fraction = (item.percentage / 100f).coerceIn(0.02f, 1f))
                                                            .height(7.dp)
                                                            .clip(RoundedCornerShape(4.dp))
                                                            .background(preset.color)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C5CE7),
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text("Close", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    )

    if (showCategoryBillsModal && selectedGraphCategory != null) {
        val activeCategory = selectedGraphCategory!!
        val matchingBills = remember(allExpenses, activeCategory.categoryName) {
            allExpenses.filter { expense ->
                val expCat = if (expense.category.isNotBlank() && !expense.category.equals("General", ignoreCase = true)) {
                    expense.category
                } else {
                    ExpenseCategories.getCategoryForExpense(expense.description)
                }
                expCat.equals(activeCategory.categoryName, ignoreCase = true) || expense.description.contains(activeCategory.categoryName, ignoreCase = true)
            }
        }

        CategoryBillsModal(
            categoryName = activeCategory.categoryName,
            categoryBills = matchingBills,
            categoryTotal = activeCategory.totalAmount,
            onDismiss = { showCategoryBillsModal = false }
        )
    }
}

/**
 * Modal Pop-up displaying all individual bills inside a selected category when the Eye icon is clicked.
 */
@Composable
fun CategoryBillsModal(
    categoryName: String,
    categoryBills: List<Expense>,
    categoryTotal: Double,
    onDismiss: () -> Unit
) {
    val preset = ExpenseCategories.getPresetForCategory(categoryName)

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForestSurface,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(preset.color),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = ExpenseCategories.getCategoryIcon(preset.iconName),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = categoryName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryOnDark,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "${categoryBills.size} bill${if (categoryBills.size != 1) "s" else ""} • -${CurrencyUtils.formatPounds(categoryTotal)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = preset.color,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        },
        text = {
            if (categoryBills.isEmpty()) {
                Text(
                    text = "No bills found in this category.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondaryOnDark
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categoryBills, key = { it.id }) { expense ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = ForestSurfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = expense.description,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimaryOnDark,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Group: ${expense.groupName}${if (expense.dueDate.isNotBlank()) " • Due: ${expense.dueDate}" else ""}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondaryOnDark,
                                        fontSize = 11.5.sp
                                    )
                                    if (expense.notes.isNotBlank()) {
                                        Text(
                                            text = "Note: ${expense.notes}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFF7B61FF),
                                            fontSize = 11.sp
                                        )
                                    }
                                }

                                Text(
                                    text = "-${CurrencyUtils.formatPounds(expense.amount)}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = preset.color,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6C5CE7),
                    contentColor = Color.White
                )
            ) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        }
    )
}

/**
 * Dialog to manage & switch between different Monthly Expense Lists (e.g., "This Month", "Next Month")
 * and create fresh unstamped copies for next month's planning without losing current bills.
 */
@Composable
fun ExpenseListsDialog(
    activeListId: String,
    expenseLists: List<ExpenseListInfo>,
    allExpenses: List<Expense>,
    onSelectList: (String) -> Unit,
    onCreateNextMonthList: (String) -> Unit,
    onPromoteListToLive: (String) -> Unit = {},
    onDeleteList: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var newListNameInput by remember { mutableStateOf("") }
    var showCreateInput by remember { mutableStateOf(false) }
    var listToDelete by remember { mutableStateOf<ExpenseListInfo?>(null) }
    var listToPromote by remember { mutableStateOf<ExpenseListInfo?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForestSurface,
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(EntryTealContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = EntryTeal,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Monthly Expense Lists",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryOnDark
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Work out next month's bills without losing this month's",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondaryOnDark,
                        fontSize = 11.5.sp
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // List of Available Monthly Lists
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(expenseLists, key = { it.id }) { listInfo ->
                        val isActive = listInfo.id == activeListId
                        val listExpenses = remember(allExpenses, listInfo.id) {
                            allExpenses.filter { it.listId == listInfo.id || (listInfo.id == "current" && (it.listId.isBlank() || it.listId == "current")) }
                        }
                        val listTotal = remember(listExpenses) {
                            listExpenses.sumOf { it.amount }
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    onSelectList(listInfo.id)
                                    onDismiss()
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isActive) EntryTealContainer else ForestSurfaceVariant
                            ),
                            border = BorderStroke(
                                width = if (isActive) 1.5.dp else 1.dp,
                                color = if (isActive) EntryTeal else ForestCardBorder
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Text(
                                            text = listInfo.name,
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isActive) EntryTeal else TextPrimaryOnDark,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f, fill = false)
                                        )

                                        if (isActive) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(EntryTeal)
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = "ACTIVE",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = OnEntryTeal,
                                                    fontSize = 9.sp,
                                                    maxLines = 1,
                                                    softWrap = false
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${listExpenses.size} bills • -${CurrencyUtils.formatPounds(listTotal)}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondaryOnDark,
                                        fontSize = 11.5.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    if (listInfo.id != "current") {
                                        // "Upgrade to Live" Button
                                        Button(
                                            onClick = { listToPromote = listInfo },
                                            shape = RoundedCornerShape(8.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = IncomeGreenContainer,
                                                contentColor = IncomeGreen
                                            ),
                                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                            modifier = Modifier.height(28.dp)
                                        ) {
                                            Text(
                                                text = "Upgrade to Live ⚡",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp,
                                                maxLines = 1,
                                                softWrap = false
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(4.dp))

                                        IconButton(
                                            onClick = { listToDelete = listInfo },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.DeleteOutline,
                                                contentDescription = "Delete List",
                                                tint = WarningRed,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (showCreateInput) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = newListNameInput,
                            onValueChange = { newListNameInput = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("e.g. Next Month, December 2026", color = TextMutedOnDark.copy(alpha = 0.6f)) },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = ForestSurfaceVariant,
                                unfocusedContainerColor = ForestSurfaceVariant,
                                focusedBorderColor = EntryTeal,
                                unfocusedBorderColor = ForestCardBorder,
                                focusedTextColor = TextPrimaryOnDark,
                                unfocusedTextColor = TextPrimaryOnDark
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = { showCreateInput = false },
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Cancel", color = TextSecondaryOnDark)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    val name = newListNameInput.ifBlank { "Next Month" }
                                    onCreateNextMonthList(name)
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = EntryTeal,
                                    contentColor = OnEntryTeal
                                )
                            ) {
                                Text("Create & Switch", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    Button(
                        onClick = {
                            newListNameInput = "Next Month"
                            showCreateInput = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EntryTeal,
                            contentColor = OnEntryTeal
                        ),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "+ Create Next Month's Unstamped List",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ForestCardBorder))
            ) {
                Text("Close", color = TextSecondaryOnDark, fontWeight = FontWeight.Bold)
            }
        }
    )

    listToDelete?.let { targetList ->
        DeleteMonthListConfirmationDialog(
            listInfo = targetList,
            onConfirmDelete = {
                onDeleteList(targetList.id)
                listToDelete = null
            },
            onDismiss = { listToDelete = null }
        )
    }

    listToPromote?.let { targetList ->
        ConfirmUpgradeToLiveDialog(
            listInfo = targetList,
            onConfirmUpgrade = {
                onPromoteListToLive(targetList.id)
                listToPromote = null
                onDismiss()
            },
            onDismiss = { listToPromote = null }
        )
    }
}

/**
 * Confirmation Dialog before promoting a secondary prepared list to Live.
 */
@Composable
fun ConfirmUpgradeToLiveDialog(
    listInfo: ExpenseListInfo,
    onConfirmUpgrade: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForestSurface,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(IncomeGreenContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text("⚡", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Upgrade to Live?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryOnDark
                )
            }
        },
        text = {
            Text(
                text = "This will promote all prepared bills in \"${listInfo.name}\" to your active Live expense list. Your current Live expenses will be replaced with these prepared bills.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondaryOnDark
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirmUpgrade,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = IncomeGreen,
                    contentColor = TextPrimaryOnDark
                )
            ) {
                Text("Upgrade to Live ⚡", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ForestCardBorder))
            ) {
                Text("Cancel", color = TextSecondaryOnDark)
            }
        }
    )
}

/**
 * Confirmation Dialog before deleting a Monthly Expense List.
 */
@Composable
fun DeleteMonthListConfirmationDialog(
    listInfo: ExpenseListInfo,
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForestSurface,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = null,
                    tint = WarningRed,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Delete Monthly List?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimaryOnDark
                )
            }
        },
        text = {
            Text(
                text = "Are you sure you want to delete \"${listInfo.name}\"? This will permanently delete this monthly list and all bills created within it.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondaryOnDark
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirmDelete,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = WarningRed,
                    contentColor = TextPrimaryOnDark
                )
            ) {
                Text("Delete List", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ForestCardBorder))
            ) {
                Text("Cancel", color = TextSecondaryOnDark)
            }
        }
    )
}

