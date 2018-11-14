package es.upm.miw.firebase;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import es.upm.miw.firebase.models.Delivery;

public class DeliveryAdapter extends ArrayAdapter implements View.OnClickListener {

    private Context contexto;
    private List<Delivery> listaPedidos;
    private int idRecursoLayout;

    public DeliveryAdapter(Context context, int resource, List<Delivery> listaPedidos) {
        super(context, resource, listaPedidos);
        this.contexto = context;
        this.idRecursoLayout = resource;
        this.listaPedidos = listaPedidos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ConstraintLayout vista;

        if (null != convertView) {
            vista = (ConstraintLayout) convertView;
        } else {
            LayoutInflater inflador = (LayoutInflater) contexto
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vista = (ConstraintLayout) inflador.inflate(idRecursoLayout, parent, false);
        }

        Delivery deliveryObject = this.listaPedidos.get(position);
        TextView userName = (TextView) vista.findViewById(R.id.refPedido);
        userName.setText(String.valueOf(deliveryObject.getId()));

        TextView fecha = (TextView) vista.findViewById(R.id.fecha);
        fecha.setText(String.valueOf(deliveryObject.getFechaRegistro()));

        return vista;
    }

    @Override
    public void onClick(View v) {

    }
}
