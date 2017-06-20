package com.pixelplex.qtum.ui.fragment.SetYourTokenFragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.dataprovider.RestAPI.gsonmodels.Contract.ContractMethodParameter;
import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.datastorage.model.ContractTemplate;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.FontTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by kirillvolkov on 26.05.17.
 */

public class SetYourTokenFragment extends BaseFragment implements SetYourTokenFragmentView {

    public final int LAYOUT = R.layout.fragment_set_your_token;
    public final static String CONTRACT_TEMPLATE_UIID = "uiid";

    private ConstructorAdapter adapter;

    public static SetYourTokenFragment newInstance(long uiid) {
        Bundle args = new Bundle();
        SetYourTokenFragment fragment = new SetYourTokenFragment();
        args.putLong(CONTRACT_TEMPLATE_UIID,uiid);
        fragment.setArguments(args);
        return fragment;
    }

    SetYourTokenFragmentPresenterImpl presenter;

    @BindView(R.id.recycler_view)
    RecyclerView constructorList;

    @BindView(R.id.tv_template_name)
    FontTextView mTextViewTemplateName;

    @OnClick({R.id.ibt_back, R.id.cancel})
    public void onBackClick() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.confirm)
    public void onConfirmClick(){
        if(adapter != null) {
           // adapter.notifyDataSetChanged();
            presenter.confirm(adapter.getParams(), getArguments().getLong(CONTRACT_TEMPLATE_UIID));
        }
    }

    @Override
    protected void createPresenter() {
        presenter = new SetYourTokenFragmentPresenterImpl(this);
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return presenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        constructorList.setLayoutManager(new LinearLayoutManager(getContext()));
        long templateUiid = getArguments().getLong(CONTRACT_TEMPLATE_UIID);
        presenter.getConstructorByUiid(templateUiid);
        String templateName = "";
        TinyDB tinyDB = new TinyDB(getContext());
        List<ContractTemplate> contractTemplateList = tinyDB.getContractTemplateList();
        for(ContractTemplate contractTemplate : contractTemplateList){
            if(contractTemplate.getUiid()==templateUiid) {
                templateName = contractTemplate.getName();
                break;
            }
        }

        mTextViewTemplateName.setText(templateName);
    }

    @Override
    public void onResume() {
        hideBottomNavView(false);
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        showBottomNavView(false);
    }

    @Override
    public void onContractConstructorPrepared(List<ContractMethodParameter> params) {
        adapter = new ConstructorAdapter(params);
        constructorList.setAdapter(adapter);
    }
}
