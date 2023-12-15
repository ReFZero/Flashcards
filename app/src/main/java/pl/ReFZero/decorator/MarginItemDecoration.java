package pl.ReFZero.decorator;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class MarginItemDecoration extends RecyclerView.ItemDecoration {

    private int margin;


    public MarginItemDecoration(int margin) {

        this.margin = margin;

    }


    @Override

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        outRect.set(margin, margin, margin, margin);

    }
}