

package zh.com.dialog_picker;



final class LoopRunnable implements Runnable {

    final LoopView loopView;

    LoopRunnable(LoopView loopview) {
        super();
        loopView = loopview;

    }

    public final void run() {
        LoopListener listener = loopView.loopListener;
        int i = LoopView.getSelectItem(loopView);
        listener.onItemSelect(i);
    }
}
