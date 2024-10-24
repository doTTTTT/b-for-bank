package fr.dot.feature.menu.screen

internal sealed interface MenuAction {

    data class MenuSelect(val menu: MenuItem) : MenuAction

}