package de.jumpingpxl.labymod.chattime.listener;

import de.jumpingpxl.labymod.chattime.JumpingAddon;
import net.labymod.api.events.MessageModifyChatEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.HoverEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Nico (JumpingPxl) Middendorf
 * @date 21.12.2018
 */

public class MessageModifyChatListener implements MessageModifyChatEvent {

	private JumpingAddon jumpingAddon;

	public MessageModifyChatListener(JumpingAddon jumpingAddon) {
		this.jumpingAddon = jumpingAddon;
	}

	@Override
	public Object onModifyChatMessage(Object object) {
		if (!jumpingAddon.getSettings().isEnabledChatTime())
			return object;
		String time = "";
		try {
			time = new SimpleDateFormat(jumpingAddon.getSettings().stripColor('&',
					jumpingAddon.getSettings().getChatTimeFormat())).format(new Date(System.currentTimeMillis()));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		TextComponentString chatComponent = new TextComponentString(jumpingAddon.getSettings().
				translateAlternateColorCodes('&', jumpingAddon.getSettings().getChatTimeStyle()).replace("%time%", time));
		if (jumpingAddon.getSettings().isEnabledHover())
			chatComponent.setStyle(new Style().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
					new TextComponentString(jumpingAddon.getSettings().translateAlternateColorCodes('&',
					jumpingAddon.getSettings().getHoverStyle()).replace("%time%", time)))));
		if (jumpingAddon.getSettings().isBeforeMessage())
			return new TextComponentString("").setStyle(new Style()).appendSibling(chatComponent).
					appendSibling((ITextComponent) object);
		else
			return ((ITextComponent) object).appendSibling(chatComponent);
	}
}
