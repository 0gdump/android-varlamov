package open.v0gdump.varlamov.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import open.v0gdump.varlamov.R

class CollectionsFragment : Fragment() {

    private lateinit var layout: View

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        layout = inflater.inflate(
            R.layout.fragment_collections,
            container,
            false
        )

        return layout
    }
}
