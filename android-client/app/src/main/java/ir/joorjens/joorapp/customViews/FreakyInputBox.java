package ir.joorjens.joorapp.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import ir.joorjens.joorapp.R;

/**
 * Created by mohsen on 4/13/2018.
 */

public class FreakyInputBox extends FrameLayout{

    private TextViewPlus _tvFibName;
    private TextViewPlus _tvFibActionName;
    private EditTextPlus _etFibValue;
    private Boolean _showAction;
    private Boolean _readOnly;
    private OnClickListener _onClickListener;

    public void setOnClickListener(OnClickListener _clickListener) {
        this._onClickListener = _clickListener;
    }

    public void setValue(String value){
        _etFibValue.setText(value);
    }
    public String getValue(){
       return  _etFibValue.getText().toString();
    }

    public FreakyInputBox(@NonNull Context context) {
        super(context);
        init(null);
    }

    public FreakyInputBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FreakyInputBox(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        View view = inflate(getContext(), R.layout.view_freaky_input_box, null);
        _tvFibName = (TextViewPlus) view.findViewById(R.id.fib_tv_name);
        _tvFibActionName = (TextViewPlus) view.findViewById(R.id.fib_tv_action);
        _etFibValue = (EditTextPlus) view.findViewById(R.id.fib_et_value);

        if (attrs != null){
            TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.FreakyInputBox);
            _tvFibName.setText(attributes.getString(R.styleable.FreakyInputBox_fib_name));
            _tvFibActionName.setText(attributes.getString(R.styleable.FreakyInputBox_fib_action_name));
            _etFibValue.setText(attributes.getString(R.styleable.FreakyInputBox_fib_value));
            _showAction = attributes.getBoolean(R.styleable.FreakyInputBox_fib_show_action, false);
            if(!_showAction) _tvFibActionName.setText("");
            _readOnly = attributes.getBoolean(R.styleable.FreakyInputBox_fib_read_only, false);
            _etFibValue.setEnabled(!_readOnly);
            _etFibValue.setInputType( attributes.getInt(R.styleable.FreakyInputBox_android_inputType, 0) );

        }

        addView(view);
    }
}
