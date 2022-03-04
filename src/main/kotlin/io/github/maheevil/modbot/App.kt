package io.github.maheevil.modbot

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.utils.env
import com.soywiz.korio.dynamic.KDynamic.Companion.toLong
import dev.kord.common.entity.Snowflake
import io.github.maheevil.modbot.extensions.moderation.auto_moderation.AntiScamProt
import io.github.maheevil.modbot.extensions.moderation.auto_moderation.RaidProt
import io.github.maheevil.modbot.extensions.moderation.logging.LogEventListener
import io.github.maheevil.modbot.extensions.moderation.util.ModerationCommands
import io.github.maheevil.modbot.extensions.util.MiscCommands
import io.github.maheevil.modbot.extensions.util.suggestions.SuggestionCommand
import io.github.maheevil.modbot.util.config.GuildConfigData
import io.github.maheevil.modbot.util.config.deserializeAndLoadFromJson

var guildConfigDataMap = HashMap<Long,GuildConfigData>()

val TEST_SERVER_ID = Snowflake(env("TEST_SERVER").toLong())
private val TOKEN = env("TOKEN")

suspend fun main() {
    val bot = ExtensibleBot(TOKEN) {
        chatCommands {
            defaultPrefix = "!"
            enabled = true
        }
        extensions {
            add(::AntiScamProt)
            add(::ModerationCommands)
            add(::RaidProt)
            add(::LogEventListener)
            add(::SuggestionCommand)
            add(::MiscCommands)

            // KordEx extra modules
            //extMappings {}
            //extPhishing { appName = "Untitled-Moderation-Bot" }
        }
    }

    //guildConfigDataMap[TEST_SERVER_ID.toLong()] = GuildConfigData(joinLeaveLogChannelID, alertLogsChannelID, modLogsChannelID, inviteCode )
    deserializeAndLoadFromJson()
    //serializeAndSaveToJson()
    bot.start()
}