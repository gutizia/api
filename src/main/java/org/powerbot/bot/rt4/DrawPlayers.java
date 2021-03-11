package org.powerbot.bot.rt4;

import org.powerbot.bot.rt4.client.internal.IPlayer;
import org.powerbot.script.*;
import org.powerbot.script.rt4.ClientAccessor;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.*;

import java.awt.*;

public class DrawPlayers extends ClientAccessor implements PaintListener {
	public DrawPlayers(final ClientContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("deprecation")
	public void repaint(final Graphics render) {
		if (ctx.game.clientState() != Constants.GAME_LOADED) {
			return;
		}
		final Player me = ctx.players.local();;
		final FontMetrics metrics = render.getFontMetrics();
		for (final IPlayer player : ctx.players.toStream().list()) {
			try {
				if (!player.inViewport()) {
					continue;
				}
				final Point location = player.centerPoint();
				if (location.x == -1 || location.y == -1) {
					continue;
				}
				if (player.tile().distanceTo(me) <= 2) {
					player.drawModel(render);
				}

				final Polygon hull = player.hull();
				if (hull != null) {
					render.drawPolygon(hull);
				}

				render.setColor(Color.RED);
				render.fillRect((int) location.getX() - 1, (int) location.getY() - 1, 2, 2);
				String s = player.name() + " (" + player.getCombatLevel() + " [" + player.healthPercent() + "])";
				render.setColor(player.inCombat() ? Color.RED : player.inMotion() ? Color.GREEN : Color.WHITE);
				render.drawString(s, location.x - metrics.stringWidth(s) / 2, location.y - metrics.getHeight() / 2);
				final String msg = player.getOverheadMessage();
				boolean raised = false;
				if (player.getAnimation() != -1) {
					s = "";
					s += "(";
					if (player.getAnimation() != -1) {
						s += "A: " + player.getAnimation() + " | ST: -1 | ";
					}

					s = s.substring(0, s.lastIndexOf(" | "));
					s += ")";

					render.drawString(s, location.x - metrics.stringWidth(s) / 2, location.y - metrics.getHeight() * 3 / 2);
					raised = true;
				}
				if (msg != null) {
					render.setColor(Color.ORANGE);
					render.drawString(msg, location.x - metrics.stringWidth(msg) / 2, location.y - metrics.getHeight() * (raised ? 5 : 3) / 2);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
