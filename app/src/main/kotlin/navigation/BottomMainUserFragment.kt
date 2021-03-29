package navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.R

class BottomMainUserFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // var(가변) -> val(불변, final)
        val view = LayoutInflater.from(activity).inflate(R.layout.fragment_bottom_main_user, container, false)
        return view
    }
}