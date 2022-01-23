package io.github.maheevil.modbot.extensions.moderation.util

import com.kotlindiscord.kord.extensions.utils.respond
import com.kotlindiscord.kord.extensions.utils.selfMember
import com.kotlindiscord.kord.extensions.utils.timeoutUntil
import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.GuildBehavior
import dev.kord.core.behavior.UserBehavior
import dev.kord.core.behavior.ban
import dev.kord.core.behavior.edit
import dev.kord.core.entity.Message
import dev.kord.core.entity.channel.GuildMessageChannel
import dev.kord.rest.request.auditLogReason
import io.github.maheevil.modbot.extensions.moderation.logging.createModLog
import io.github.maheevil.modbot.modLogsChannelID
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

suspend fun createBanWithLog(meessage: Message,guild: GuildBehavior, moderator: UserBehavior, target: Snowflake, banReason: String?){
    guild.ban(target){reason = banReason}
    meessage.respond(
            "Banned ${guild.kord.getUser(target)?.mention}, Reason given : $banReason"
    )
    createModLog(guild.getChannel(modLogsChannelID) as GuildMessageChannel,"banned",moderator.id,target,banReason, Color(0xff0000))
}

suspend fun removeBanWithLog(meessage: Message,guild: GuildBehavior, moderator: UserBehavior, target: Snowflake, unbanReason: String?){
    guild.unban(target,unbanReason)
    meessage.respond(
            "Unbanned ${guild.kord.getUser(target)?.mention}, Reason given : $unbanReason"
    )
    createModLog(guild.getChannel(modLogsChannelID) as GuildMessageChannel,"unbanned",moderator.id,target,unbanReason, Color(0x09850b))
}

suspend fun kickUserWithLog(meessage: Message?, guild: GuildBehavior, moderator: UserBehavior, target: Snowflake, kickReason: String?){
    guild.kick(target,kickReason)
    meessage?.respond(
            "Kicked ${guild.kord.getUser(target)?.mention}, Reason given : $kickReason"
    )
    createModLog(guild.getChannel(modLogsChannelID) as GuildMessageChannel,"kicked",moderator.id,target,kickReason, Color(0xff5e00))
}

suspend fun timeoutUserWithLog(meessage: Message?, guild: GuildBehavior, moderator: UserBehavior, target: Snowflake, duration: Duration, reason: String?){
    guild.getMember(target).edit {
        this.reason = reason
        timeoutUntil =  Clock.System.now() + duration
    }
    meessage?.respond(
            "timedout ${guild.kord.getUser(target)?.mention},Duration = $duration, Reason given : $reason"
    )
    createModLog(guild.getChannel(modLogsChannelID) as GuildMessageChannel,"timedout",moderator.id,target,reason, Color(0xd9d904),duration)
}