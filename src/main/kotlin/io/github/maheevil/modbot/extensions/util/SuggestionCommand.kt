/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 */
package io.github.maheevil.modbot.extensions.util

import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.commands.application.slash.publicSubCommand
import com.kotlindiscord.kord.extensions.commands.converters.impl.coalescingDefaultingString
import com.kotlindiscord.kord.extensions.commands.converters.impl.coalescingString
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.kotlindiscord.kord.extensions.utils.addReaction
import dev.kord.common.Color
import dev.kord.core.entity.channel.TextChannel
import dev.kord.rest.builder.message.EmbedBuilder
import kotlinx.datetime.Clock


class SuggestionCommand : Extension() {
    override val name = "suggestion"

    override suspend fun setup() {
        publicSlashCommand {
            name = "suggestion"
            description = "suggestion related commands"

            publicSubCommand(::CreateSubCommandArgs){
                name = "create"
                description = "create a suggestion"

                action{
                    if (guild == null){
                        respond { content = "This is a guild only command" }
                        return@action
                    }

                    val messageID = respond {
                        this.embeds.add(createSuggestionEmbed(arguments.title,arguments.suggestionString,Color(0x09850b)))
                    }.message.id

                    channel.getMessage(messageID).addReaction("\uD83D\uDC4D")
                    channel.getMessage(messageID).addReaction("\uD83D\uDC4E")

                    (channel.asChannel() as TextChannel).startPublicThreadWithMessage(messageID,arguments.title).addUser(user.id)
                }
            }
        }
    }

    inner class CreateSubCommandArgs : Arguments(){
        val suggestionString by coalescingString{
            name = "suggestion"
            description = "the content of the suggestion"
        }
        val title by coalescingDefaultingString{
            name ="title"
            description = "Title at the top of the embed"
            defaultValue = "Suggestion"
        }
    }

    private fun createSuggestionEmbed(title: String, suggestionString: String, colour: Color, response: String? = null) : EmbedBuilder {
        val embed = EmbedBuilder()
        embed.title = title
        embed.color = colour
        embed.field("Suggestion",false){suggestionString}
        embed.field("votes",false){"0"}
        embed.timestamp = Clock.System.now()
        return embed
    }
}