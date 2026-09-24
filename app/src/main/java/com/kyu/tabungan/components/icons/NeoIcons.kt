package com.kyu.tabungan.components.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object NeoIcons {
    private fun vector(
        name: String,
        block: ImageVector.Builder.() -> Unit
    ): ImageVector {
        return ImageVector.Builder(
            name = name,
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply(block).build()
    }

    val Home: ImageVector by lazy {
        vector("Home") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3f, 9.5f)
                lineTo(12f, 3f)
                lineTo(21f, 9.5f)
                verticalLineTo(20f)
                curveTo(21f, 20.55f, 20.55f, 21f, 20f, 21f)
                horizontalLineTo(15f)
                verticalLineTo(14f)
                horizontalLineTo(9f)
                verticalLineTo(21f)
                horizontalLineTo(4f)
                curveTo(3.45f, 21f, 3f, 20.55f, 3f, 20f)
                close()
            }
        }
    }

    val Chart: ImageVector by lazy {
        vector("Chart") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(18f, 20f)
                verticalLineTo(10f)
                moveTo(12f, 20f)
                verticalLineTo(4f)
                moveTo(6f, 20f)
                verticalLineTo(14f)
            }
        }
    }

    val Plus: ImageVector by lazy {
        vector("Plus") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 3f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 5f)
                verticalLineTo(19f)
                moveTo(5f, 12f)
                horizontalLineTo(19f)
            }
        }
    }

    val Wallet: ImageVector by lazy {
        vector("Wallet") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(19f, 7f)
                verticalLineTo(4f)
                curveTo(19f, 3.45f, 18.55f, 3f, 18f, 3f)
                horizontalLineTo(5f)
                curveTo(3.9f, 3f, 3f, 3.9f, 3f, 5f)
                verticalLineTo(19f)
                curveTo(3f, 20.1f, 3.9f, 21f, 5f, 21f)
                horizontalLineTo(19f)
                curveTo(20.1f, 21f, 21f, 20.1f, 21f, 19f)
                verticalLineTo(9f)
                curveTo(21f, 7.9f, 20.1f, 7f, 19f, 7f)
                close()
                moveTo(3f, 7f)
                horizontalLineTo(19f)
                moveTo(16f, 14f)
                horizontalLineTo(16.01f)
            }
        }
    }

    val Menu: ImageVector by lazy {
        vector("Menu") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4f, 6f)
                horizontalLineTo(20f)
                moveTo(4f, 12f)
                horizontalLineTo(20f)
                moveTo(4f, 18f)
                horizontalLineTo(20f)
            }
        }
    }

    val ArrowDown: ImageVector by lazy {
        vector("ArrowDown") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 5f)
                verticalLineTo(19f)
                moveTo(19f, 12f)
                lineTo(12f, 19f)
                lineTo(5f, 12f)
            }
        }
    }

    val ArrowUp: ImageVector by lazy {
        vector("ArrowUp") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 19f)
                verticalLineTo(5f)
                moveTo(5f, 12f)
                lineTo(12f, 5f)
                lineTo(19f, 12f)
            }
        }
    }

    val Food: ImageVector by lazy {
        vector("Food") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(18f, 2f)
                verticalLineTo(22f)
                moveTo(15f, 2f)
                curveTo(15f, 5f, 18f, 6f, 18f, 8f)
                moveTo(6f, 2f)
                verticalLineTo(8f)
                curveTo(6f, 10f, 8f, 11f, 8f, 11f)
                verticalLineTo(22f)
                moveTo(10f, 2f)
                verticalLineTo(8f)
                moveTo(4f, 2f)
                verticalLineTo(8f)
            }
        }
    }

    val Transport: ImageVector by lazy {
        vector("Transport") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5f, 17f)
                horizontalLineTo(19f)
                moveTo(5f, 17f)
                lineTo(3f, 11f)
                lineTo(5f, 5f)
                horizontalLineTo(19f)
                lineTo(21f, 11f)
                lineTo(19f, 17f)
                moveTo(7f, 17f)
                verticalLineTo(19f)
                moveTo(17f, 17f)
                verticalLineTo(19f)
                moveTo(7f, 11f)
                horizontalLineTo(7.01f)
                moveTo(17f, 11f)
                horizontalLineTo(17.01f)
            }
        }
    }

    val Shopping: ImageVector by lazy {
        vector("Shopping") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 2f)
                lineTo(3f, 6f)
                verticalLineTo(20f)
                curveTo(3f, 21.1f, 3.9f, 22f, 5f, 22f)
                horizontalLineTo(19f)
                curveTo(20.1f, 22f, 21f, 21.1f, 21f, 20f)
                verticalLineTo(6f)
                lineTo(18f, 2f)
                horizontalLineTo(6f)
                close()
                moveTo(3f, 6f)
                horizontalLineTo(21f)
                moveTo(16f, 10f)
                curveTo(16f, 12.2f, 14.2f, 14f, 12f, 14f)
                curveTo(9.8f, 14f, 8f, 12.2f, 8f, 10f)
            }
        }
    }

    val Entertainment: ImageVector by lazy {
        vector("Entertainment") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 12f)
                horizontalLineTo(10f)
                moveTo(8f, 10f)
                verticalLineTo(14f)
                moveTo(15f, 13f)
                horizontalLineTo(15.01f)
                moveTo(18f, 11f)
                horizontalLineTo(18.01f)
                moveTo(2f, 6f)
                curveTo(2f, 4.9f, 2.9f, 4f, 4f, 4f)
                horizontalLineTo(20f)
                curveTo(21.1f, 4f, 22f, 4.9f, 22f, 6f)
                verticalLineTo(18f)
                curveTo(22f, 19.1f, 21.1f, 20f, 20f, 20f)
                horizontalLineTo(4f)
                curveTo(2.9f, 20f, 2f, 19.1f, 2f, 18f)
                close()
            }
        }
    }

    val Bills: ImageVector by lazy {
        vector("Bills") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4f, 2f)
                verticalLineTo(22f)
                lineTo(7f, 20f)
                lineTo(10f, 22f)
                lineTo(13f, 20f)
                lineTo(16f, 22f)
                lineTo(20f, 20f)
                verticalLineTo(2f)
                lineTo(16f, 4f)
                lineTo(13f, 2f)
                lineTo(10f, 4f)
                lineTo(7f, 2f)
                close()
                moveTo(8f, 8f)
                horizontalLineTo(16f)
                moveTo(8f, 12f)
                horizontalLineTo(16f)
                moveTo(8f, 16f)
                horizontalLineTo(12f)
            }
        }
    }

    val Education: ImageVector by lazy {
        vector("Education") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(22f, 10f)
                verticalLineTo(16f)
                moveTo(2f, 10f)
                lineTo(12f, 5f)
                lineTo(22f, 10f)
                lineTo(12f, 15f)
                lineTo(2f, 10f)
                close()
                moveTo(6f, 12.5f)
                verticalLineTo(17.5f)
                curveTo(6f, 17.5f, 8f, 20f, 12f, 20f)
                curveTo(16f, 20f, 18f, 17.5f, 18f, 17.5f)
                verticalLineTo(12.5f)
            }
        }
    }

    val Health: ImageVector by lazy {
        vector("Health") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(22f, 12f)
                horizontalLineTo(18f)
                lineTo(15f, 21f)
                lineTo(9f, 3f)
                lineTo(6f, 12f)
                horizontalLineTo(2f)
            }
        }
    }

    val Salary: ImageVector by lazy {
        vector("Salary") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(2f, 6f)
                curveTo(2f, 4.9f, 2.9f, 4f, 4f, 4f)
                horizontalLineTo(20f)
                curveTo(21.1f, 4f, 22f, 4.9f, 22f, 6f)
                verticalLineTo(18f)
                curveTo(22f, 19.1f, 21.1f, 20f, 20f, 20f)
                horizontalLineTo(4f)
                curveTo(2.9f, 20f, 2f, 19.1f, 2f, 18f)
                close()
                moveTo(12f, 9f)
                curveTo(10.3f, 9f, 9f, 10.3f, 9f, 12f)
                curveTo(9f, 13.7f, 10.3f, 15f, 12f, 15f)
                curveTo(13.7f, 15f, 15f, 13.7f, 15f, 12f)
                curveTo(15f, 10.3f, 13.7f, 9f, 12f, 9f)
                close()
                moveTo(6f, 12f)
                horizontalLineTo(6.01f)
                moveTo(18f, 12f)
                horizontalLineTo(18.01f)
            }
        }
    }

    val Freelance: ImageVector by lazy {
        vector("Freelance") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(16f, 18f)
                lineTo(22f, 12f)
                lineTo(16f, 6f)
                moveTo(8f, 6f)
                lineTo(2f, 12f)
                lineTo(8f, 18f)
            }
        }
    }

    val Business: ImageVector by lazy {
        vector("Business") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3f, 21f)
                horizontalLineTo(21f)
                moveTo(3f, 7f)
                horizontalLineTo(21f)
                moveTo(4f, 7f)
                lineTo(5f, 3f)
                horizontalLineTo(19f)
                lineTo(20f, 7f)
                moveTo(5f, 21f)
                verticalLineTo(7f)
                moveTo(19f, 21f)
                verticalLineTo(7f)
                moveTo(9f, 21f)
                verticalLineTo(15f)
                horizontalLineTo(15f)
                verticalLineTo(21f)
            }
        }
    }

    val Gift: ImageVector by lazy {
        vector("Gift") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(20f, 12f)
                verticalLineTo(22f)
                horizontalLineTo(4f)
                verticalLineTo(12f)
                moveTo(2f, 7f)
                horizontalLineTo(22f)
                verticalLineTo(12f)
                horizontalLineTo(2f)
                close()
                moveTo(12f, 22f)
                verticalLineTo(7f)
                moveTo(12f, 7f)
                curveTo(12f, 7f, 8f, 7f, 7.5f, 4.5f)
                curveTo(7f, 2f, 9.5f, 2f, 12f, 5.5f)
                curveTo(14.5f, 2f, 17f, 2f, 16.5f, 4.5f)
                curveTo(16f, 7f, 12f, 7f, 12f, 7f)
            }
        }
    }

    val Bank: ImageVector by lazy {
        vector("Bank") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3f, 21f)
                horizontalLineTo(21f)
                moveTo(3f, 10f)
                horizontalLineTo(21f)
                moveTo(12f, 2f)
                lineTo(2f, 7f)
                verticalLineTo(10f)
                horizontalLineTo(22f)
                verticalLineTo(7f)
                lineTo(12f, 2f)
                close()
                moveTo(5f, 10f)
                verticalLineTo(17f)
                moveTo(9f, 10f)
                verticalLineTo(17f)
                moveTo(15f, 10f)
                verticalLineTo(17f)
                moveTo(19f, 10f)
                verticalLineTo(17f)
            }
        }
    }

    val EWallet: ImageVector by lazy {
        vector("EWallet") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5f, 2f)
                horizontalLineTo(19f)
                curveTo(20.1f, 2f, 21f, 2.9f, 21f, 4f)
                verticalLineTo(20f)
                curveTo(21f, 21.1f, 20.1f, 22f, 19f, 22f)
                horizontalLineTo(5f)
                curveTo(3.9f, 22f, 3f, 21.1f, 3f, 20f)
                verticalLineTo(4f)
                curveTo(3f, 2.9f, 3.9f, 2f, 5f, 2f)
                close()
                moveTo(12f, 18f)
                horizontalLineTo(12.01f)
                moveTo(7f, 6f)
                horizontalLineTo(17f)
            }
        }
    }

    val Search: ImageVector by lazy {
        vector("Search") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(11f, 11f)
                moveToRelative(-8f, 0f)
                arcToRelative(8f, 8f, 0f, true, true, 16f, 0f)
                arcToRelative(8f, 8f, 0f, true, true, -16f, 0f)
                moveTo(21f, 21f)
                lineTo(16.65f, 16.65f)
            }
        }
    }

    val Filter: ImageVector by lazy {
        vector("Filter") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(22f, 3f)
                horizontalLineTo(2f)
                lineTo(10f, 12.46f)
                verticalLineTo(19f)
                lineTo(14f, 21f)
                verticalLineTo(12.46f)
                lineTo(22f, 3f)
                close()
            }
        }
    }

    val Calendar: ImageVector by lazy {
        vector("Calendar") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3f, 5f)
                curveTo(3f, 3.9f, 3.9f, 3f, 5f, 3f)
                horizontalLineTo(19f)
                curveTo(20.1f, 3f, 21f, 3.9f, 21f, 5f)
                verticalLineTo(19f)
                curveTo(21f, 20.1f, 20.1f, 21f, 19f, 21f)
                horizontalLineTo(5f)
                curveTo(3.9f, 21f, 3f, 20.1f, 3f, 19f)
                close()
                moveTo(3f, 9f)
                horizontalLineTo(21f)
                moveTo(8f, 2f)
                verticalLineTo(5f)
                moveTo(16f, 2f)
                verticalLineTo(5f)
            }
        }
    }

    val Edit: ImageVector by lazy {
        vector("Edit") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(17f, 3f)
                curveTo(17.5f, 2.5f, 18.5f, 2.5f, 19f, 3f)
                lineTo(21f, 5f)
                curveTo(21.5f, 5.5f, 21.5f, 6.5f, 21f, 7f)
                lineTo(7f, 21f)
                lineTo(3f, 21f)
                lineTo(3f, 17f)
                lineTo(17f, 3f)
                close()
            }
        }
    }

    val Trash: ImageVector by lazy {
        vector("Trash") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3f, 6f)
                horizontalLineTo(21f)
                moveTo(19f, 6f)
                verticalLineTo(20f)
                curveTo(19f, 21.1f, 18.1f, 22f, 17f, 22f)
                horizontalLineTo(7f)
                curveTo(5.9f, 22f, 5f, 21.1f, 5f, 20f)
                verticalLineTo(6f)
                moveTo(8f, 6f)
                verticalLineTo(4f)
                curveTo(8f, 2.9f, 8.9f, 2f, 10f, 2f)
                horizontalLineTo(14f)
                curveTo(15.1f, 2f, 16f, 2.9f, 16f, 4f)
                verticalLineTo(6f)
            }
        }
    }

    val ChevronRight: ImageVector by lazy {
        vector("ChevronRight") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(9f, 18f)
                lineTo(15f, 12f)
                lineTo(9f, 6f)
            }
        }
    }

    val ChevronLeft: ImageVector by lazy {
        vector("ChevronLeft") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(15f, 18f)
                lineTo(9f, 12f)
                lineTo(15f, 6f)
            }
        }
    }

    val Check: ImageVector by lazy {
        vector("Check") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(20f, 6f)
                lineTo(9f, 17f)
                lineTo(4f, 12f)
            }
        }
    }

    val Close: ImageVector by lazy {
        vector("Close") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(18f, 6f)
                lineTo(6f, 18f)
                moveTo(6f, 6f)
                lineTo(18f, 18f)
            }
        }
    }

    val Settings: ImageVector by lazy {
        vector("Settings") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 15f)
                curveTo(13.6569f, 15f, 15f, 13.6569f, 15f, 12f)
                curveTo(15f, 10.3431f, 13.6569f, 9f, 12f, 9f)
                curveTo(10.3431f, 9f, 9f, 10.3431f, 9f, 12f)
                curveTo(9f, 13.6569f, 10.3431f, 15f, 12f, 15f)
                close()
                moveTo(19.4f, 15f)
                curveTo(19.5f, 14.7f, 19.5f, 14.3f, 19.5f, 14f)
                curveTo(19.5f, 13.7f, 19.5f, 13.3f, 19.4f, 13f)
                lineTo(21.5f, 11.4f)
                lineTo(19.5f, 7.9f)
                lineTo(17f, 8.9f)
                curveTo(16.5f, 8.5f, 15.9f, 8.2f, 15.3f, 7.9f)
                lineTo(14.9f, 5.3f)
                horizontalLineTo(10.9f)
                lineTo(10.5f, 7.9f)
                curveTo(9.9f, 8.2f, 9.3f, 8.5f, 8.8f, 8.9f)
                lineTo(6.3f, 7.9f)
                lineTo(4.3f, 11.4f)
                lineTo(6.4f, 13f)
                curveTo(6.3f, 13.3f, 6.3f, 13.7f, 6.3f, 14f)
                curveTo(6.3f, 14.3f, 6.3f, 14.7f, 6.4f, 15f)
                lineTo(4.3f, 16.6f)
                lineTo(6.3f, 20.1f)
                lineTo(8.8f, 19.1f)
                curveTo(9.3f, 19.5f, 9.9f, 19.8f, 10.5f, 20.1f)
                lineTo(10.9f, 22.7f)
                horizontalLineTo(14.9f)
                lineTo(15.3f, 20.1f)
                curveTo(15.9f, 19.8f, 16.5f, 19.5f, 17f, 19.1f)
                lineTo(19.5f, 20.1f)
                lineTo(21.5f, 16.6f)
                lineTo(19.4f, 15f)
                close()
            }
        }
    }

    val Info: ImageVector by lazy {
        vector("Info") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 16f)
                verticalLineTo(12f)
                moveTo(12f, 8f)
                horizontalLineTo(12.01f)
                moveTo(22f, 12f)
                curveTo(22f, 17.52f, 17.52f, 22f, 12f, 22f)
                curveTo(6.48f, 22f, 2f, 17.52f, 2f, 12f)
                curveTo(2f, 6.48f, 6.48f, 2f, 12f, 2f)
                curveTo(17.52f, 2f, 22f, 6.48f, 22f, 12f)
                close()
            }
        }
    }

    val Budget: ImageVector by lazy {
        vector("Budget") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 22f)
                curveTo(17.5228f, 22f, 22f, 17.5228f, 22f, 12f)
                curveTo(22f, 6.47715f, 17.5228f, 2f, 12f, 2f)
                curveTo(6.47715f, 2f, 2f, 6.47715f, 2f, 12f)
                curveTo(2f, 17.5228f, 6.47715f, 22f, 12f, 22f)
                close()
                moveTo(12f, 18f)
                curveTo(15.3137f, 18f, 18f, 15.3137f, 18f, 12f)
                curveTo(18f, 8.68629f, 15.3137f, 6f, 12f, 6f)
                curveTo(8.68629f, 6f, 6f, 8.68629f, 6f, 12f)
                curveTo(6f, 15.3137f, 8.68629f, 18f, 12f, 18f)
                close()
                moveTo(12f, 14f)
                curveTo(13.1046f, 14f, 14f, 13.1046f, 14f, 12f)
                curveTo(14f, 10.8954f, 13.1046f, 10f, 12f, 10f)
                curveTo(10.8954f, 10f, 10f, 10.8954f, 10f, 12f)
                curveTo(10f, 13.1046f, 10.8954f, 14f, 12f, 14f)
                close()
            }
        }
    }

    val Download: ImageVector by lazy {
        vector("Download") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(21f, 15f)
                verticalLineTo(19f)
                curveTo(21f, 20.1f, 20.1f, 21f, 19f, 21f)
                horizontalLineTo(5f)
                curveTo(3.9f, 21f, 3f, 20.1f, 3f, 19f)
                verticalLineTo(15f)
                moveTo(7f, 10f)
                lineTo(12f, 15f)
                lineTo(17f, 10f)
                moveTo(12f, 15f)
                verticalLineTo(3f)
            }
        }
    }

    val Upload: ImageVector by lazy {
        vector("Upload") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(21f, 15f)
                verticalLineTo(19f)
                curveTo(21f, 20.1f, 20.1f, 21f, 19f, 21f)
                horizontalLineTo(5f)
                curveTo(3.9f, 21f, 3f, 20.1f, 3f, 19f)
                verticalLineTo(15f)
                moveTo(17f, 8f)
                lineTo(12f, 3f)
                lineTo(7f, 8f)
                moveTo(12f, 3f)
                verticalLineTo(15f)
            }
        }
    }

    val Other: ImageVector by lazy {
        vector("Other") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 12f)
                horizontalLineTo(12.01f)
                moveTo(19f, 12f)
                horizontalLineTo(19.01f)
                moveTo(5f, 12f)
                horizontalLineTo(5.01f)
            }
        }
    }

    val Alert: ImageVector by lazy {
        vector("Alert") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(10.29f, 3.86f)
                lineTo(1.82f, 18f)
                curveTo(1.64f, 18.3f, 1.55f, 18.65f, 1.55f, 19f)
                curveTo(1.55f, 20.1f, 2.45f, 21f, 3.55f, 21f)
                horizontalLineTo(20.45f)
                curveTo(20.8f, 21f, 21.15f, 20.91f, 21.45f, 20.73f)
                curveTo(22.4f, 20.18f, 22.73f, 18.97f, 22.18f, 18.02f)
                lineTo(13.71f, 3.86f)
                curveTo(13.37f, 3.32f, 12.78f, 3f, 12f, 3f)
                curveTo(11.22f, 3f, 10.63f, 3.32f, 10.29f, 3.86f)
                close()
                moveTo(12f, 9f)
                verticalLineTo(13f)
                moveTo(12f, 17f)
                horizontalLineTo(12.01f)
            }
        }
    }

    val EmptyIllustration: ImageVector by lazy {
        ImageVector.Builder(
            name = "EmptyIllustration",
            defaultWidth = 120.dp,
            defaultHeight = 120.dp,
            viewportWidth = 120f,
            viewportHeight = 120f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF111111))
            ) {
                moveTo(36f, 36f)
                lineTo(92f, 36f)
                curveTo(96f, 36f, 98f, 38f, 98f, 42f)
                lineTo(98f, 90f)
                curveTo(98f, 94f, 96f, 96f, 92f, 96f)
                lineTo(36f, 96f)
                curveTo(32f, 96f, 30f, 94f, 30f, 90f)
                lineTo(30f, 42f)
                curveTo(30f, 38f, 32f, 36f, 36f, 36f)
                close()
            }
            path(
                fill = SolidColor(Color.White),
                stroke = SolidColor(Color(0xFF111111)),
                strokeLineWidth = 3.5f,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(30f, 30f)
                lineTo(86f, 30f)
                curveTo(90f, 30f, 92f, 32f, 92f, 36f)
                lineTo(92f, 84f)
                curveTo(92f, 88f, 90f, 90f, 86f, 90f)
                lineTo(30f, 90f)
                curveTo(26f, 90f, 24f, 88f, 24f, 84f)
                lineTo(24f, 36f)
                curveTo(24f, 32f, 26f, 30f, 30f, 30f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF1687FF)),
                stroke = SolidColor(Color(0xFF111111)),
                strokeLineWidth = 3f,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(64f, 48f)
                lineTo(86f, 48f)
                curveTo(89f, 48f, 92f, 51f, 92f, 54f)
                lineTo(92f, 66f)
                curveTo(92f, 69f, 89f, 72f, 86f, 72f)
                lineTo(64f, 72f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFFFFD84D)),
                stroke = SolidColor(Color(0xFF111111)),
                strokeLineWidth = 2.5f
            ) {
                moveTo(78f, 60f)
                moveToRelative(-4f, 0f)
                arcToRelative(4f, 4f, 0f, true, true, 8f, 0f)
                arcToRelative(4f, 4f, 0f, true, true, -8f, 0f)
            }
        }.build()
    }

    val Goal: ImageVector by lazy {
        vector("Goal") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 2f)
                curveTo(6.48f, 2f, 2f, 6.48f, 2f, 12f)
                curveTo(2f, 17.52f, 6.48f, 22f, 12f, 22f)
                curveTo(17.52f, 22f, 22f, 17.52f, 22f, 12f)
                close()
                moveTo(12f, 6f)
                curveTo(8.69f, 6f, 6f, 8.69f, 6f, 12f)
                curveTo(6f, 15.31f, 8.69f, 18f, 12f, 18f)
                curveTo(15.31f, 18f, 18f, 15.31f, 18f, 12f)
                close()
                moveTo(12f, 10f)
                curveTo(10.9f, 10f, 10f, 10.9f, 10f, 12f)
                curveTo(10f, 13.1f, 10.9f, 14f, 12f, 14f)
                curveTo(13.1f, 14f, 14f, 13.1f, 14f, 12f)
                close()
            }
        }
    }

    val Phone: ImageVector by lazy {
        vector("Phone") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(7f, 2f)
                horizontalLineTo(17f)
                curveTo(18.1f, 2f, 19f, 2.9f, 19f, 4f)
                verticalLineTo(20f)
                curveTo(19f, 21.1f, 18.1f, 22f, 17f, 22f)
                horizontalLineTo(7f)
                curveTo(5.9f, 22f, 5f, 21.1f, 5f, 20f)
                verticalLineTo(4f)
                curveTo(5f, 2.9f, 5.9f, 2f, 7f, 2f)
                close()
                moveTo(12f, 18f)
                horizontalLineTo(12.01f)
            }
        }
    }

    val Laptop: ImageVector by lazy {
        vector("Laptop") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4f, 6f)
                horizontalLineTo(20f)
                verticalLineTo(15f)
                horizontalLineTo(4f)
                close()
                moveTo(2f, 18f)
                horizontalLineTo(22f)
            }
        }
    }

    val Vehicle: ImageVector by lazy {
        vector("Vehicle") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(5f, 17f)
                horizontalLineTo(19f)
                moveTo(5f, 17f)
                lineTo(3f, 11f)
                lineTo(5f, 5f)
                horizontalLineTo(19f)
                lineTo(21f, 11f)
                lineTo(19f, 17f)
            }
        }
    }

    val House: ImageVector by lazy {
        vector("House") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3f, 10f)
                lineTo(12f, 3f)
                lineTo(21f, 10f)
                verticalLineTo(20f)
                horizontalLineTo(4f)
                close()
            }
        }
    }

    val Camera: ImageVector by lazy {
        vector("Camera") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4f, 7f)
                horizontalLineTo(7f)
                lineTo(9f, 4f)
                horizontalLineTo(15f)
                lineTo(17f, 7f)
                horizontalLineTo(20f)
                curveTo(21.1f, 7f, 22f, 7.9f, 22f, 9f)
                verticalLineTo(19f)
                curveTo(22f, 20.1f, 21.1f, 21f, 20f, 21f)
                horizontalLineTo(4f)
                curveTo(2.9f, 21f, 2f, 20.1f, 2f, 19f)
                verticalLineTo(9f)
                close()
                moveTo(12f, 10f)
                curveTo(10.34f, 10f, 9f, 11.34f, 9f, 13f)
                curveTo(9f, 14.66f, 10.34f, 16f, 12f, 16f)
                curveTo(13.66f, 16f, 15f, 14.66f, 15f, 13f)
                close()
            }
        }
    val Bell: ImageVector by lazy {
        vector("Bell") {
            path(
                stroke = SolidColor(Color.Black),
                strokeLineWidth = 2.5f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(18f, 8f)
                curveTo(18f, 6.4f, 17.36f, 4.88f, 16.24f, 3.76f)
                curveTo(15.12f, 2.64f, 13.6f, 2f, 12f, 2f)
                curveTo(10.4f, 2f, 8.88f, 2.64f, 7.76f, 3.76f)
                curveTo(6.64f, 4.88f, 6f, 6.4f, 6f, 8f)
                curveTo(6f, 15f, 3f, 17f, 3f, 17f)
                horizontalLineTo(21f)
                curveTo(21f, 17f, 18f, 15f, 18f, 8f)
                close()
                moveTo(13.73f, 21f)
                curveTo(13.55f, 21.3f, 13.3f, 21.55f, 13f, 21.73f)
                curveTo(12.7f, 21.9f, 12.35f, 22f, 12f, 22f)
                curveTo(11.65f, 22f, 11.3f, 21.9f, 11f, 21.73f)
                curveTo(10.7f, 21.55f, 10.45f, 21.3f, 10.27f, 21f)
            }
        }
    }

    fun getGoalIcon(iconName: String): ImageVector {
        return when (iconName.lowercase()) {
            "phone", "hp" -> Phone
            "laptop", "komputer" -> Laptop
            "motor", "car", "kendaraan", "vehicle" -> Vehicle
            "house", "rumah" -> House
            "camera", "foto" -> Camera
            "trip", "liburan" -> Transport
            "gold", "emas", "investasi" -> Budget
            else -> Goal
        }
    }

    fun getCategoryIcon(iconName: String): ImageVector {
        return when (iconName.lowercase()) {
            "food", "makanan" -> Food
            "transport", "transportasi" -> Transport
            "shopping", "belanja" -> Shopping
            "entertainment", "hiburan" -> Entertainment
            "bills", "tagihan" -> Bills
            "education", "pendidikan" -> Education
            "health", "kesehatan" -> Health
            "salary", "gaji" -> Salary
            "freelance" -> Freelance
            "business", "bisnis" -> Business
            "gift", "hadiah" -> Gift
            "bank" -> Bank
            "cash", "tunai" -> Salary
            "ewallet", "e-wallet" -> EWallet
            else -> Other
        }
    }

    fun getWalletIcon(iconName: String): ImageVector {
        return when (iconName.lowercase()) {
            "cash", "tunai" -> Salary
            "bank" -> Bank
            "ewallet", "e-wallet" -> EWallet
            else -> Wallet
        }
    }
}
