package com.moneysight.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moneysight.app.model.ExpenseGroup
import com.moneysight.app.ui.theme.EntryBorder
import com.moneysight.app.ui.theme.EntrySurface
import com.moneysight.app.ui.theme.EntryTeal
import com.moneysight.app.ui.theme.EntryTealContainer
import com.moneysight.app.ui.theme.ForestCardBorder
import com.moneysight.app.ui.theme.ForestSurface
import com.moneysight.app.ui.theme.ForestSurfaceVariant
import com.moneysight.app.ui.theme.OnEntryTeal
import com.moneysight.app.ui.theme.TextMutedOnDark
import com.moneysight.app.ui.theme.TextPrimaryOnDark
import com.moneysight.app.ui.theme.TextSecondaryOnDark
import com.moneysight.app.util.ExpenseCategory

@Composable
fun AddExpenseSection(
    descriptionInput: String,
    amountInput: String,
    dueDateInput: String = "",
    selectedGroupId: String,
    groups: List<ExpenseGroup>,
    activeCategories: List<ExpenseCategory>,
    isSubmitting: Boolean,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onDueDateChanged: (String) -> Unit = {},
    onSelectGroup: (String) -> Unit,
    onOpenManageGroupsClicked: () -> Unit,
    onOpenManagePresetsClicked: () -> Unit,
    onAddExpenseClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var showCategoryBrowser by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = EntrySurface),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.SolidColor(EntryBorder))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Section Header (Collapsible)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = null,
                        tint = EntryTeal,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Where is your money going?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryOnDark
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isExpanded) {
                        // Browse All Categories Presets Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(EntryTealContainer)
                                .clickable { showCategoryBrowser = true }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.GridView,
                                    contentDescription = "Browse Categories",
                                    tint = EntryTeal,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Presets",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = EntryTeal
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }

                    // Collapse / Expand Toggle
                    IconButton(
                        onClick = onToggleExpand,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "Collapse Form" else "Expand Form",
                            tint = EntryTeal
                        )
                    }
                }
            }

            // Expanded Section Form
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 14.dp)
                ) {
                    // Group / Person Selector Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Assign to Group / Person:",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondaryOnDark,
                            fontSize = 11.sp
                        )

                        Text(
                            text = "+ Manage Groups",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = EntryTeal,
                            fontSize = 11.sp,
                            modifier = Modifier.clickable { onOpenManageGroupsClicked() }
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Group Choice Chips Row
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
                                    .background(if (isSelected) EntryTeal else ForestSurfaceVariant)
                                    .clickable { onSelectGroup(group.id) }
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = group.name,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) OnEntryTeal else TextPrimaryOnDark,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Add Group Quick Chip
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(EntryTealContainer)
                                .clickable { onOpenManageGroupsClicked() }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.GroupAdd,
                                    contentDescription = "New Group",
                                    tint = EntryTeal,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "New Group",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = EntryTeal,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Description Input
                    OutlinedTextField(
                        value = descriptionInput,
                        onValueChange = onDescriptionChanged,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("What is this expense for?", color = TextSecondaryOnDark) },
                        placeholder = { Text("e.g. Rent, Grocery shopping, Electric bill", color = TextMutedOnDark.copy(alpha = 0.6f)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
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

                    Spacer(modifier = Modifier.height(12.dp))

                    // Due Date Input (Optional)
                    OutlinedTextField(
                        value = dueDateInput,
                        onValueChange = onDueDateChanged,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Date bill is coming out (optional)", color = TextSecondaryOnDark) },
                        placeholder = { Text("e.g. 1st, 15th, 28th", color = TextMutedOnDark.copy(alpha = 0.6f)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
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

                    Spacer(modifier = Modifier.height(12.dp))

                    // Amount Input & Add Button Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = amountInput,
                            onValueChange = { input ->
                                val clean = input.replace(",", ".")
                                if (clean.isEmpty() || clean.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                                    onAmountChanged(clean)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            label = { Text("Amount (£)", color = TextSecondaryOnDark) },
                            placeholder = { Text("0.00", color = TextMutedOnDark.copy(alpha = 0.6f)) },
                            prefix = { Text("£ ", fontWeight = FontWeight.Bold, color = EntryTeal) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    keyboardController?.hide()
                                    onAddExpenseClicked()
                                }
                            ),
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

                        val isValidInput = descriptionInput.isNotBlank() && amountInput.toDoubleOrNull()?.let { it > 0 } == true

                        Button(
                            onClick = {
                                keyboardController?.hide()
                                onAddExpenseClicked()
                            },
                            enabled = isValidInput && !isSubmitting,
                            modifier = Modifier
                                .height(56.dp)
                                .align(Alignment.Bottom),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EntryTeal,
                                contentColor = OnEntryTeal,
                                disabledContainerColor = ForestCardBorder,
                                disabledContentColor = TextSecondaryOnDark
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add Expense",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Add",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Full Category Presets Modal Browser Dialog
    if (showCategoryBrowser) {
        AlertDialog(
            onDismissRequest = { showCategoryBrowser = false },
            containerColor = ForestSurface,
            shape = RoundedCornerShape(20.dp),
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = null,
                            tint = EntryTeal,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Expense Presets",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryOnDark
                        )
                    }

                    Text(
                        text = "+ Manage",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = EntryTeal,
                        modifier = Modifier.clickable {
                            showCategoryBrowser = false
                            onOpenManagePresetsClicked()
                        }
                    )
                }
            },
            text = {
                if (activeCategories.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No active presets selected",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryOnDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Manage your presets to select or add custom presets to show here.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondaryOnDark,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                showCategoryBrowser = false
                                onOpenManagePresetsClicked()
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = EntryTeal,
                                contentColor = OnEntryTeal
                            )
                        ) {
                            Text("Manage Presets", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 380.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(activeCategories, key = { it.name }) { category ->
                            Column {
                                Text(
                                    text = category.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = EntryTeal,
                                    letterSpacing = 0.8.sp
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    category.items.forEach { item ->
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(ForestSurfaceVariant)
                                                .clickable {
                                                    onDescriptionChanged(item)
                                                    showCategoryBrowser = false
                                                }
                                                .padding(horizontal = 12.dp, vertical = 10.dp)
                                        ) {
                                            Text(
                                                text = item,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = TextPrimaryOnDark,
                                                fontWeight = FontWeight.Medium
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
                    onClick = { showCategoryBrowser = false },
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
}
