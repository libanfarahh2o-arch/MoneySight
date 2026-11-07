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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.PieChart
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
import com.moneysight.app.ui.theme.OnChecklistPurple
import com.moneysight.app.ui.theme.OnEntryTeal
import com.moneysight.app.ui.theme.ThemeMode
import com.moneysight.app.util.ExpenseCategories
import com.moneysight.app.util.ExpenseCategory
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moneysight.app.model.Expense
import com.moneysight.app.model.ExpenseGroup
import com.moneysight.app.model.IncomeSource
import com.moneysight.app.ui.theme.ForestCardBorder
import com.moneysight.app.ui.theme.ForestSurface
import com.moneysight.app.viewmodel.CategoryBreakdownData
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
 * Edit Expense Dialog.
 */
@Composable
fun EditExpenseDialog(
    descriptionInput: String,
    amountInput: String,
    notesInput: String = "",
    dueDateInput: String = "",
    selectedCategory: String = "Expenses",
    selectedGroupId: String,
    groups: List<ExpenseGroup>,
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
                text = "Edit Expense",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextPrimaryOnDark
            )
        },
        text = {
            Column {
                // Category Choice Row
                Text(
                    text = "Category Preset:",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondaryOnDark,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ExpenseCategories.categoryGridPresets.forEach { preset ->
                        val isSelected = selectedCategory.equals(preset.title, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) preset.color else ForestSurfaceVariant)
                                .clickable { onCategoryChanged(preset.title) }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = preset.title,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextPrimaryOnDark,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Group Choice Chips
                Text(
                    text = "Group / Person:",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondaryOnDark,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    groups.forEach { group ->
                        val isSelected = selectedGroupId == group.id
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) MintGreenPrimary else ForestSurfaceVariant)
                                .clickable { onGroupIdChanged(group.id) }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = group.name,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) OnMintGreen.let { FontWeight.Bold } else FontWeight.Medium,
                                color = if (isSelected) OnMintGreen else TextPrimaryOnDark,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = descriptionInput,
                    onValueChange = onDescriptionChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Description", color = TextSecondaryOnDark) },
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
                        unfocusedTextColor = TextPrimaryOnDark,
                        cursorColor = MintGreenPrimary
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

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

                Spacer(modifier = Modifier.height(12.dp))

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
            Button(
                onClick = onConfirm,
                enabled = descriptionInput.isNotBlank() && amountInput.toDoubleOrNull()?.let { it > 0 } == true,
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
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onDelete != null) {
                    Button(
                        onClick = onDelete,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = WarningRed,
                            contentColor = TextPrimaryOnDark
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DeleteOutline,
                                contentDescription = "Delete",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Delete", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(10.dp),
                    border = ButtonDefaults.outlinedButtonBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(ForestCardBorder))
                ) {
                    Text("Cancel", color = TextSecondaryOnDark)
                }
            }
        }
    )
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
 * Dialog to Manage Expense Presets (Selecting built-in presets & adding/deleting custom extra presets).
 */
@Composable
fun ManagePresetsDialog(
    combinedCategories: List<ExpenseCategory>,
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
    val totalAvailable = combinedCategories.sumOf { it.items.size }
    val totalSelected = selectedPresets.size

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForestSurface,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.GridView,
                    contentDescription = null,
                    tint = EntryTeal,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Manage Presets",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryOnDark
                    )
                    Text(
                        text = "Select presets to show in 'Where is your money going?'",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondaryOnDark,
                        fontSize = 11.sp
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
                                color = TextMutedOnDark.copy(alpha = 0.6f),
                                fontSize = 13.sp
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { onAddCustomPresetClicked() }),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = ForestSurfaceVariant,
                            unfocusedContainerColor = ForestSurfaceVariant,
                            focusedBorderColor = EntryTeal,
                            unfocusedBorderColor = ForestCardBorder,
                            focusedTextColor = TextPrimaryOnDark,
                            unfocusedTextColor = TextPrimaryOnDark,
                            cursorColor = EntryTeal
                        )
                    )

                    Button(
                        onClick = onAddCustomPresetClicked,
                        enabled = customPresetInput.isNotBlank(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EntryTeal,
                            contentColor = OnEntryTeal,
                            disabledContainerColor = ForestCardBorder,
                            disabledContentColor = TextSecondaryOnDark
                        )
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add Extra",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Extra", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Control Row: Select All / Deselect All / Count
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$totalSelected / $totalAvailable selected",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = EntryTeal,
                        fontSize = 11.sp
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "Select All",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = EntryTeal,
                            fontSize = 11.sp,
                            modifier = Modifier.clickable { onSelectAllClicked() }
                        )

                        Text(
                            text = "Deselect All",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondaryOnDark,
                            fontSize = 11.sp,
                            modifier = Modifier.clickable { onDeselectAllClicked() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Scrollable List of Categories & Items
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 440.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(combinedCategories, key = { it.name }) { category ->
                        Column {
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = EntryTeal,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Column(
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                category.items.forEach { item ->
                                    val isSelected = selectedPresets.contains(item)
                                    val isCustom = customPresets.any { it.equals(item, ignoreCase = true) }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(ForestSurfaceVariant)
                                            .clickable { onTogglePresetSelected(item) }
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Checkbox(
                                                checked = isSelected,
                                                onCheckedChange = { onTogglePresetSelected(item) },
                                                colors = CheckboxDefaults.colors(
                                                    checkedColor = EntryTeal,
                                                    uncheckedColor = TextSecondaryOnDark,
                                                    checkmarkColor = OnEntryTeal
                                                )
                                            )
                                            Text(
                                                text = item,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = if (isSelected) TextPrimaryOnDark else TextMutedOnDark,
                                                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                                                fontSize = 13.sp
                                            )
                                        }

                                        if (isCustom) {
                                            IconButton(
                                                onClick = { onRemoveCustomPresetClicked(item) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.DeleteOutline,
                                                    contentDescription = "Delete Extra Preset",
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
                Text("Done", fontWeight = FontWeight.Bold)
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
 * Visual "Where Your Money Goes" Analytics Modal Dialog for Simulate Screen.
 * Displays category/preset expense distribution with percentage progress bars.
 */
@Composable
fun SimulateGraphDialog(
    categoryBreakdowns: List<CategoryBreakdownData>,
    totalExpenses: Double,
    onDismiss: () -> Unit
) {
    val categoryColors = listOf(
        IncomeGreen,
        EntryTeal,
        ChecklistPurplePrimary,
        ExpenseRose,
        WarningAmber,
        MintGreenPrimary
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForestSurface,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.PieChart,
                    contentDescription = null,
                    tint = ChecklistPurplePrimary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Where Your Money Goes",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryOnDark
                    )
                    Text(
                        text = "Category breakdown synced with bills & presets",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondaryOnDark,
                        fontSize = 11.sp
                    )
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Total Expense Summary Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = ChecklistSurfaceVariant)
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
                                color = ChecklistPurpleLight,
                                fontSize = 10.sp,
                                letterSpacing = 0.8.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "-${CurrencyUtils.formatPounds(totalExpenses)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextPrimaryOnDark
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(ChecklistPurpleContainer)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${categoryBreakdowns.size} Categories",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = ChecklistPurpleLight,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (categoryBreakdowns.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No expenses recorded yet",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryOnDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Add expenses on the main screen to see where your money goes.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondaryOnDark,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    // Category Breakdown Progress Bars List
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 320.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(categoryBreakdowns, key = { _, item -> item.categoryName }) { index, item ->
                            val color = categoryColors[index % categoryColors.size]

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = ForestSurfaceVariant)
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp)
                                ) {
                                    // Category Header: Name, Count, Amount & Percentage
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(10.dp)
                                                    .clip(CircleShape)
                                                    .background(color)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = item.categoryName,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimaryOnDark,
                                                fontSize = 13.sp
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "(${item.billCount} ${if (item.billCount == 1) "bill" else "bills"})",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = TextSecondaryOnDark,
                                                fontSize = 10.sp
                                            )
                                        }

                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = "-${CurrencyUtils.formatPounds(item.totalAmount)}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimaryOnDark,
                                                fontSize = 13.sp
                                            )
                                            Text(
                                                text = String.format(java.util.Locale.UK, "%.1f%%", item.percentage),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = color,
                                                fontSize = 11.sp
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    // Visual Percentage Bar
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp)
                                            .clip(RoundedCornerShape(4.dp))
                                            .background(ForestSurface)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(fraction = (item.percentage / 100f).coerceIn(0.02f, 1f))
                                                .height(8.dp)
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(color)
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
                    containerColor = ChecklistPurplePrimary,
                    contentColor = OnChecklistPurple
                )
            ) {
                Text("Close", fontWeight = FontWeight.Bold)
            }
        }
    )
}
