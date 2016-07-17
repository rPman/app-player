
package org.luwrain.app.player;

import org.luwrain.core.*;
import org.luwrain.core.events.*;
import org.luwrain.controls.*;
import org.luwrain.player.*;

class ControlArea extends NavigationArea
{
    private Luwrain luwrain;
    private Actions actions;
    private Strings strings;
    private Base base;

    private String opPlayPause, opStop, opPrevTrack, opNextTrack;

    private long timeSec = -1;

ControlArea(Luwrain luwrain, Actions actions,
	       Strings strings)
    {
	super(new DefaultControlEnvironment(luwrain));
	NullCheck.notNull(luwrain, "luwrain");
	NullCheck.notNull(actions, "actions");
	NullCheck.notNull(strings, "strigns");
	this.luwrain = luwrain;
	this.actions = actions;
	this.strings = strings;
	base = actions.getBase();
	opPlayPause = strings.opPlayPause();
	opStop = strings.opStop();
	opPrevTrack = strings.opPrevTrack();
	opNextTrack = strings.opNextTrack();
    }

    void onNewPlaylist(Playlist playlist)
    {
    }

    void onNewTrack(int trackNum)
    {
    }

    void onTrackTime(long msec)
    {
	long sec = msec / 1000;
	if (sec != timeSec)
	{
	timeSec = sec;
	luwrain.onAreaNewContent(this);
	}
    }

    void onStop()
    {
    }

    @Override public int getLineCount()
    {
	int count = 0;
	if (!base.getCurrentPlaylistTitle().isEmpty())
	    ++count;
	if (!base.getCurrentTrackTitle().isEmpty())
	    ++count;
	if (timeSec >= 0)
	    ++count;
	return count + 6;
    }

    @Override public String getLine(int index)
    {
	int offset = 0;
	if (base.getCurrentPlaylistTitle().isEmpty())
	    ++offset;
	if (base.getCurrentTrackTitle().isEmpty())
	    ++offset;
	if (timeSec < 0)
	    ++offset;
	switch(index + offset)
	{
	case 0:
	    return base.getCurrentPlaylistTitle();
	case 1:
	    return base.getCurrentTrackTitle();
	case 2:
	    return Base.getTimeStr(timeSec);
	case 3:
	    return "";
	case 4:
	    return opPlayPause;
	case 5:
	    return opStop;
	case 6:
	    return opPrevTrack;
	case 7:
	    return opNextTrack;
	default:
	    return "";
	}
    }

		@Override public boolean onKeyboardEvent(KeyboardEvent event)
		{
		    NullCheck.notNull(event, "event");
		    if (event.isSpecial() && !event.isModified())
			switch(event.getSpecial())
			{
			case TAB:
			    actions.goToTree();
			    return true;
			case BACKSPACE:
			    actions.goToTree();
			    return true;
			}
		    return super.onKeyboardEvent(event);
		}

    @Override public boolean onEnvironmentEvent(EnvironmentEvent event)
    {
	NullCheck.notNull(event, "event");
	switch(event.getCode())
	{
	case CLOSE:
	    actions.closeApp();
	    return true;
	default:
	    return super.onEnvironmentEvent(event);
	}
    }

    @Override public String getAreaName()
    {
	return strings.controlAreaName();
    }

    @Override public void announceLine(int index, String line)
    {
	if (line == opPlayPause || line == opStop ||
line == opPrevTrack || line == opNextTrack)
	    luwrain.playSound(Sounds.LIST_ITEM);
	if (line.isEmpty())
	    luwrain.hint(Hints.EMPTY_LINE); else
	    luwrain.say(line);
    }
}
