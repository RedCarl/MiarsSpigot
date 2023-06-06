// From
// https://github.com/Argarian-Network/NachoSpigot/tree/async-kb-hit
package ga.windpvp.windspigot.async.thread;

public class KnockbackThread extends AsyncPacketThread {
    public KnockbackThread(String s) {
        super(s);
    }

    @Override
    public void run() {
        while (this.packets.size() > 0) {
            this.packets.poll().run();
        }
    }
} 
