package vn.mb360.flightticket.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.mb360.flightticket.network.model.Ticket;
import vn.mb360.myflighttickets.R;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {

    private Context context;
    private List<Ticket> ticketList;
    private TicketAdapterListener listener;

    public TicketAdapter(Context context, List<Ticket> ticketList, TicketAdapterListener listener) {
        this.context = context;
        this.ticketList = ticketList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ticket_row, parent, false);
        
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Ticket ticket = ticketList.get(position);
        Glide.with(context)
                .load(ticket.getAirline().getLogo())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.logo);
        holder.airlineName.setText(String.format("%s", ticket.getAirline().getName()));
        holder.departure.setText(String.format("%s Dep", ticket.getDeparture()));
        holder.arrival.setText(String.format("%s Dest", ticket.getArrival()));

        holder.duration.setText(ticket.getFlightNumber());
        holder.duration.append(", " + ticket.getDuration());
        holder.stops.setText(String.format(Locale.US, "%d Stops", ticket.getNumberOfStops()));

        if (!TextUtils.isEmpty(ticket.getInstructions())) {
            holder.duration.append(", " + ticket.getInstructions());
        }

        if (ticket.getPrice() != null) {
            holder.price.setText(String.format("₹%s", String.format(Locale.US, "%.0f", ticket.getPrice().getPrice())));
            holder.seats.setText(String.format("%s Seats", ticket.getPrice().getSeats()));
            holder.loader.setVisibility(View.INVISIBLE);
        } else {
            holder.loader.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return ticketList.size();
    }

    public interface TicketAdapterListener {
        void onTicketSelected(Ticket ticket);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.airlineName)
        TextView airlineName;
        @BindView(R.id.logo)
        ImageView logo;
        @BindView(R.id.numberOfStops)
        TextView stops;
        @BindView(R.id.numberOfSeats)
        TextView seats;
        @BindView(R.id.departure)
        TextView departure;
        @BindView(R.id.arrival)
        TextView arrival;
        @BindView(R.id.duration)
        TextView duration;
        @BindView(R.id.price)
        TextView price;
        @BindView(R.id.loader)
        SpinKitView loader;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onTicketSelected(ticketList.get(getAdapterPosition()));
                }
            });
        }
    }
}
