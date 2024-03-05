package days

import corbella83.aoc.Day
import corbella83.aoc.Resource
import corbella83.aoc.Result
import corbella83.aoc.utils.replace

object D7 : Day {

    private enum class Bet { NONE, TWO, TWO_PLUS_TWO, THREE, THREE_PLUS_TWO, FOUR, FIVE }

    private class Hand(
        val cards: CharArray,
        val bid: Long,
        val bet: Bet
    )

    override val day = 7

    override fun part1(resource: Resource): Result {
        val cardOrder = charArrayOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
        return resource.readLines { parseHand(it, false) }
            .sortedWith(HandCompare(cardOrder))
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
    }

    override fun part2(resource: Resource): Result {
        val cardOrder = charArrayOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

        return resource.readLines { parseHand(it, true) }
            .sortedWith(HandCompare(cardOrder))
            .mapIndexed { index, hand -> (index + 1) * hand.bid }
            .sum()
    }

    private fun parseHand(hand: String, joker: Boolean): Hand {
        val parts = hand.split(' ')
        val cards = parts[0].toCharArray()

        return Hand(
            cards,
            parts[1].toLong(),
            if (joker) parseResultWithJoker(cards) else parseResult(cards)
        )
    }

    private fun parseResultWithJoker(game: CharArray): Bet {
        return if (game.contains('J')) {
            game.distinct()
                .map { game.replace('J', it) }
                .map { parseResult(it) }
                .maxBy { it.ordinal }
        } else {
            parseResult(game)
        }
    }

    private fun parseResult(game: CharArray): Bet {
        val same = game.groupBy { it }.map { it.value.size }
        return when {
            same.contains(5) -> Bet.FIVE
            same.contains(4) -> Bet.FOUR
            same.contains(3) -> if (same.contains(2)) Bet.THREE_PLUS_TWO else Bet.THREE
            same.contains(2) -> if (same.size == 3) Bet.TWO_PLUS_TWO else Bet.TWO
            else -> Bet.NONE
        }
    }

    private class HandCompare(private val cardOrder: CharArray) : Comparator<Hand> {
        override fun compare(o1: Hand, o2: Hand): Int {
            val comp = o1.bet.ordinal - o2.bet.ordinal
            if (comp != 0) {
                return comp
            } else {
                for (i in o1.cards.indices) {
                    val diff = cardOrder.indexOf(o2.cards[i]) - cardOrder.indexOf(o1.cards[i])
                    if (diff != 0) return diff
                }
                throw Exception()
            }
        }
    }
}
