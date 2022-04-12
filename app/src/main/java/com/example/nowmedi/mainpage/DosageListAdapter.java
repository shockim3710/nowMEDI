package com.example.nowmedi.mainpage;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nowmedi.R;

import java.util.ArrayList;

public class DosageListAdapter extends BaseAdapter {

    ArrayList<DosageListAdapterData> list = new ArrayList<DosageListAdapterData>();

    @Override
    public int getCount() {
        return list.size(); //그냥 배열의 크기를 반환하면 됨
    }

    @Override
    public Object getItem(int i) {
        return list.get(i); //배열에 아이템을 현재 위치값을 넣어 가져옴
    }

    @Override
    public long getItemId(int i) {
        return i; //그냥 위치값을 반환해도 되지만 원한다면 아이템의 num 을 반환해도 된다.
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;
        final Context context = viewGroup.getContext();

        //리스트뷰에 아이템이 인플레이트 되어있는지 확인한후
        //아이템이 없다면 아래처럼 아이템 레이아웃을 인플레이트 하고 view객체에 담는다.
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dosage_listview_item, viewGroup, false);
        }

        //이제 아이템에 존재하는 텍스트뷰 객체들을 view객체에서 찾아 가져온다
        TextView mediName = (TextView) view.findViewById(R.id.item_medi_name);
        TextView mediAlarm = (TextView) view.findViewById(R.id.item_medi_alarm);

        //현재 포지션에 해당하는 아이템에 글자를 적용하기 위해 list배열에서 객체를 가져온다.
        DosageListAdapterData listdata = list.get(i);

        //가져온 객체안에 있는 글자들을 각 뷰에 적용한다
        mediName.setText(listdata.getMedi_name());
        mediAlarm.setText(listdata.getMedi_alarm());


        LinearLayout cmdArea= (LinearLayout)view.findViewById(R.id.medi_choice);
        cmdArea.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //해당 리스트 클릭시 이벤트

                Intent intent = new Intent(
                        v.getContext(), // 현재화면의 제어권자
                        MediDetail.class); // 다음넘어갈 화면

                intent.putExtra("mediName", list.get(pos).getMedi_name());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                v.getContext().startActivity(intent);

            }
        });

        return view;
    }

    //ArrayList로 선언된 list 변수에 목록을 채워주기 위함 다른방시으로 구현해도 됨
    public void addItemToList(String name, String alarm) {
        DosageListAdapterData listdata = new DosageListAdapterData();

        listdata.setMedi_name(name);
        listdata.setMedi_alarm(alarm);

        //값들의 조립이 완성된 listdata객체 한개를 list배열에 추가
        list.add(listdata);

    }
}
