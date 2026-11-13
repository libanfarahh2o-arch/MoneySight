package com.moneysight.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moneysight.app.model.ExpenseGroup
import com.moneysight.app.ui.theme.EntryBorder
import com.moneysight.app.ui.theme.EntrySurface
import com.moneysight.app.ui.theme.EntryTeal
import com.moneysight.app.ui.theme.ForestSurfaceVariant
import com.moneysight.app.ui.theme.LocalMoneySightColors
import com.moneysight.app.ui.theme.OnEntryTeal
import com.moneysight.app.ui.theme.TextMutedOnDark
import com.moneysight.app.ui.theme.TextPrimaryOnDark
import com.moneysight.app.ui.theme.TextSecondaryOnDark
import com.moneysight.app.util.ExpenseCategories
import com.moneysight.app.util.ExpenseCategory

@Composable
fun AddExpenseSection(
    descriptionInput: String,
    amountInput: String,
    dueDateInput: String = "",
    selectedCategory: String = "General",
    selectedGroupId: String,
    groups: List<ExpenseGroup>,
    selectedPresets: Set<String> = emptySet(),
    customPresets: List<String> = emptyList(),
    isSubmitting: Boolean,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onDueDateChanged: (String) -> Unit = {},
    onSelectCategoryChanged: (String) -> Unit = {},
    onSelectGroup: (String) -> Unit,
    onOpenManageGroupsClicked: () -> Unit,
    onOpenManagePresetsClicked: () -> Unit,
    onAddExpenseClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var showCategoryBrowser by remember { mutableStateOf(false) }
    var showGroupBrowser by remember { mutableStateOf(false) }
    var showDateDialog by remember { mutableStateOf(false) }

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

    val selectedPreset = remember(selectedCategory) {
        ExpenseCategories.getPresetForCategory(selectedCategory)
    }

    val isDark = LocalMoneySightColors.current.isDark
    val cardBg = if (isDark) EntrySurface else Color(0xFFEBF8F5)
    val cardBorderColor = if (isDark) EntryBorder else Color(0xFFA2E2D2)
    val inputFieldBg = if (isDark) ForestSurfaceVariant else Color(0xFFE2ECE8)
    val textPrimaryColor = if (isDark) TextPrimaryOnDark else Color(0xFF0B2822)
    val textSecondaryColor = if (isDark) TextSecondaryOnDark else Color(0xFF4B635C)
    val tealAccentColor = if (isDark) EntryTeal else Color(0xFF00796B)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(1.5.dp, cardBorderColor)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Header Row: Title, Chevron, Subtitle
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Where is your money going?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = textPrimaryColor,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Expense details",
                        style = MaterialTheme.typography.bodyMedium,
                        color = textSecondaryColor,
                        fontSize = 14.sp
                    )
                }

                IconButton(
                    onClick = onToggleExpand,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse Form" else "Expand Form",
                        tint = textPrimaryColor,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // Expanded Form Content
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 1. Description Input ("What is this expense for?")
                    OutlinedTextField(
                        value = descriptionInput,
                        onValueChange = onDescriptionChanged,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                "What is this expense for?",
                                color = TextMutedOnDark,
                                fontSize = 15.sp
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = inputFieldBg,
                            unfocusedContainerColor = inputFieldBg,
                            focusedBorderColor = tealAccentColor,
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = textPrimaryColor,
                            unfocusedTextColor = textPrimaryColor,
                            cursorColor = tealAccentColor
                        )
                    )

                    // 2. Middle Row: Category Box, Group Box, Calendar Square Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Category Box
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(58.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(inputFieldBg)
                                .clickable { showCategoryBrowser = true }
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
                                        color = textSecondaryColor,
                                        fontSize = 11.5.sp
                                    )
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = textPrimaryColor,
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
                                        text = selectedCategory,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = textPrimaryColor,
                                        fontSize = 13.5.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                        // Group Box
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(58.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(inputFieldBg)
                                .clickable { showGroupBrowser = true }
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
                                        color = textSecondaryColor,
                                        fontSize = 11.5.sp
                                    )
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        tint = textPrimaryColor,
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
                                        tint = tealAccentColor,
                                        modifier = Modifier.size(18.dp)
                                    )

                                    Text(
                                        text = selectedGroup.name,
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = textPrimaryColor,
                                        fontSize = 13.5.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                        // Calendar Date Square Button
                        Box(
                            modifier = Modifier
                                .size(width = 58.dp, height = 58.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(inputFieldBg)
                                .clickable { showDateDialog = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Due Date",
                                tint = tealAccentColor,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    // 3. Bottom Row: Amount & "+ Add" Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "AMOUNT (£)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0B2822),
                                fontSize = 11.sp,
                                letterSpacing = 0.5.sp
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            OutlinedTextField(
                                value = amountInput,
                                onValueChange = { input ->
                                    val clean = input.replace(",", ".")
                                    if (clean.isEmpty() || clean.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                                        onAmountChanged(clean)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(
                                        "0.00",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF329883).copy(alpha = 0.6f),
                                        fontSize = 26.sp
                                    )
                                },
                                prefix = {
                                    Text(
                                        "£",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF329883),
                                        fontSize = 26.sp
                                    )
                                },
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
                                shape = RoundedCornerShape(16.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedTextColor = Color(0xFF329883),
                                    unfocusedTextColor = Color(0xFF329883),
                                    cursorColor = Color(0xFF00796B)
                                )
                            )
                        }

                        val isValidInput = descriptionInput.isNotBlank() && amountInput.toDoubleOrNull()?.let { it > 0 } == true

                        Button(
                            onClick = {
                                keyboardController?.hide()
                                onAddExpenseClicked()
                            },
                            enabled = isValidInput && !isSubmitting,
                            modifier = Modifier
                                .height(48.dp)
                                .width(110.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFC7D8D2),
                                contentColor = Color(0xFF0B2822),
                                disabledContainerColor = Color(0xFFE2ECE8),
                                disabledContentColor = Color(0xFF8DA39B)
                            )
                        ) {
                            Text(
                                text = "+ Add",
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

    // Category Presets Modal Browser Dialog (All 42 Presets)
    if (showCategoryBrowser) {
        AlertDialog(
            onDismissRequest = { showCategoryBrowser = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(28.dp),
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
                            tint = Color(0xFF00796B),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Select Category",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF002920)
                        )
                    }

                    Text(
                        text = "+ Manage",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00796B),
                        modifier = Modifier.clickable {
                            showCategoryBrowser = false
                            onOpenManagePresetsClicked()
                        }
                    )
                }
            },
            text = {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 400.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(availableCategoryPresets, key = { it.title }) { preset ->
                        val isSelected = selectedCategory.equals(preset.title, ignoreCase = true)

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) Color(0xFFD6F2EA) else Color.Transparent)
                                .clickable {
                                    onSelectCategoryChanged(preset.title)
                                    showCategoryBrowser = false
                                }
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
                    onClick = { showCategoryBrowser = false },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00897B),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Done",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
            }
        )
    }

    // Group Picker Modal Dialog
    if (showGroupBrowser) {
        AlertDialog(
            onDismissRequest = { showGroupBrowser = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp),
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
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

                    Text(
                        text = "+ Manage",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00796B),
                        modifier = Modifier.clickable {
                            showGroupBrowser = false
                            onOpenManageGroupsClicked()
                        }
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
                                    onSelectGroup(group.id)
                                    showGroupBrowser = false
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
                    onClick = { showGroupBrowser = false },
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

    // Due Date Dialog Modal
    if (showDateDialog) {
        AlertDialog(
            onDismissRequest = { showDateDialog = false },
            containerColor = Color.White,
            shape = RoundedCornerShape(24.dp),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color(0xFF00796B),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Due Date / Payment Day",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF002920)
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Enter the day of the month or date this bill comes out:",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF4B635C)
                    )

                    OutlinedTextField(
                        value = dueDateInput,
                        onValueChange = onDueDateChanged,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. 1st, 15th, 28th", color = Color(0xFF719688)) },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFE2ECE8),
                            unfocusedContainerColor = Color(0xFFE2ECE8),
                            focusedBorderColor = Color(0xFF00796B),
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color(0xFF0B2822),
                            unfocusedTextColor = Color(0xFF0B2822)
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showDateDialog = false },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF00897B),
                        contentColor = Color.White
                    )
                ) {
                    Text("Save Date", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}
