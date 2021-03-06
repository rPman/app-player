
package org.luwrain.player.backends;

import org.luwrain.core.*;

public interface BackEnd
{
    boolean play(Task task);
    void stop();

    static public BackEnd createBackEnd(Listener listener, String name)
    {
	NullCheck.notNull(listener, "listener");
	NullCheck.notNull(name, "name");
	switch(name.toLowerCase())
	{
	case "jlayer":
	    return new JLayer(listener);
	case "internal":
	    return new SoundPlayer(listener);
	case "jorbis":
	    return new OggPlayer(listener);
	default:
	    return null;
	}
    }
}
