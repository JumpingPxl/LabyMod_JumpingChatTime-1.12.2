package de.jumpingpxl.labymod.chattime.listener;

import de.jumpingpxl.labymod.chattime.util.Settings;
import net.labymod.api.events.MessageModifyChatEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class MessageModifyChatListener implements MessageModifyChatEvent {

	private final Settings settings;

	public MessageModifyChatListener(Settings settings) {
		this.settings = settings;
	}

	@Override
	public Object onModifyChatMessage(Object object) {
		if (!settings.isEnabledChatTime()) {
			return object;
		}

		TextComponentString textComponent = settings.getStyle().createCopy();
		for (int i = 0; i < textComponent.getSiblings().size(); i++) {
			ITextComponent sibling = textComponent.getSiblings().get(i);
			if (sibling.getUnformattedText().contains("%time%")) {
				TextComponentString newSibling = new TextComponentString(
						sibling.getUnformattedText().replace("%time%", getTime()));
				newSibling.setStyle(sibling.getStyle());
				textComponent.getSiblings().remove(sibling);
				textComponent.getSiblings().add(i, newSibling);
			}
		}

		if (settings.isBeforeMessage()) {
			return textComponent.appendSibling((ITextComponent) object);
		} else {
			return new TextComponentString("").appendSibling((ITextComponent) object).appendSibling(
					textComponent);
		}
	}

	private String getTime() {
		try {
			return settings.getDateFormat().format(System.currentTimeMillis());
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return "§cERROR";
		}
	}
}
