package ch.heigvd.asyncInputManager;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.util.concurrent.locks.ReentrantLock;


public class AsyncInputManager implements NativeKeyListener {

    private ReentrantLock mutex = new ReentrantLock() ;
    private int keyCode = 0;



    public void nativeKeyPressed(NativeKeyEvent e) {
        // do nothing;
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent e) {
        mutex.lock();
        keyCode = e.getKeyCode();
        mutex.unlock();
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent e) {
        // do nothing;
    }

    public KEY getKey() {
        mutex.lock();
        int tmp = keyCode;
        mutex.unlock();
        return KEY.getKey(tmp);
    }

    public AsyncInputManager() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("Loading native hook error : " + ex.getMessage());
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(this);
    }

    public void StopHandler(){
        GlobalScreen.removeNativeKeyListener(this);
    }

    public static void testShowKeys()  {
        AsyncInputManager keyHandler = new AsyncInputManager();

        KEY key = KEY.NONE;

        while (key != KEY.QUIT) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (key != keyHandler.getKey()) {
                key = keyHandler.getKey();
                System.out.println("Key " + key.toString() + " has been set");
            }

        }

        System.out.println("Exit showKeys");
        keyHandler.StopHandler();
    }

}
