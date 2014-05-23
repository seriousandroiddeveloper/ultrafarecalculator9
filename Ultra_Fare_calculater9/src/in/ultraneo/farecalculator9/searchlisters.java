package in.ultraneo.farecalculator9;


	import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

	import android.app.ListActivity;
	import android.app.ProgressDialog;
	import android.content.Context;
import android.content.Intent;
	import android.graphics.Bitmap;
	import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
	import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
	import android.util.Log;
import android.view.KeyEvent;
	import android.view.LayoutInflater;
	import android.view.View;
	import android.view.ViewGroup;
	import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
	import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

	public class searchlisters extends ListActivity{
	   
	    private ProgressDialog m_ProgressDialog = null;
	    private ArrayList<Order2> m_orders = null;
	    private OrderAdapter m_adapter;
	    private Runnable viewOrders;
	    ImageButton imgbtn1;
	    EditText edittxt1;
	    String POS;
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.searchlister);
	        imgbtn1 = (ImageButton)findViewById(R.id.imageButton1);
	        imgbtn1.setEnabled(false);
	        edittxt1 = (EditText)findViewById(R.id.editText1);
	        edittxt1.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if(edittxt1.getText().toString().equals(""))
					{
						imgbtn1.setEnabled(false);
					}
					else
					{
						imgbtn1.setEnabled(true);
					}
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					
				}
			});
	        
	        
	       
	        
	        Bundle bundle = getIntent().getExtras();

	        
	        final String search = bundle.getString("search"); 
	        POS = bundle.getString("pos");
	        
	        
	        if(search.equals("#"))
	        {
	        	
	        }
	        else
	        {
	        	edittxt1.setText(search);

				m_orders = new ArrayList<Order2>();
		        m_adapter = new OrderAdapter(this, R.layout.rowx, m_orders);
		        setListAdapter(m_adapter);
		       
		        viewOrders = new Runnable(){
		            @Override
		            public void run() {
		                getOrders();
		            }
		        };
		        Thread thread =  new Thread(null, viewOrders, "MagentoBackground");
		        thread.start();
		        m_ProgressDialog = ProgressDialog.show(searchlisters.this,    
		              "Please wait...", "Retrieving data ...", true);
	        	
	        }
	        
	        
	        
	        imgbtn1.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					m_orders = new ArrayList<Order2>();
			        m_adapter = new OrderAdapter(v.getContext(), R.layout.rowx, m_orders);
			        setListAdapter(m_adapter);
			       
			        viewOrders = new Runnable(){
			            @Override
			            public void run() {
			                getOrders();
			            }
			        };
			        Thread thread =  new Thread(null, viewOrders, "MagentoBackground");
			        thread.start();
			        m_ProgressDialog = ProgressDialog.show(searchlisters.this,    
			              "Please wait...", "Retrieving data ...", true);
					
				}
			});
	        
	    }
	    
	    
	    
	    public void onListItemClick(ListView parent, View v, int position,
				 long id)
	    {
	    	String Locality1,AdminArea1,Sublocality,Subadmin,admin,country;
	    	
	    	Address address = null;
	    	 address = addresses.get(position);
	    	 /*Bundle bundle = new Bundle();
	    	 
	    	
	    	
	    	 
	    	 
	    	 
	      	 bundle.putString("head1",address.getFeatureName()+","+address.getCountryCode());
	      	 bundle.putString("head2", address.getLocality()+","+address.getSubAdminArea()+","+address.getAdminArea()+address.getCountryName());
	      	 bundle.putDouble("lat1", address.getLatitude());
	      	 bundle.putDouble("lon1", address.getLongitude());*/
	      	 
	      	if(POS.equals("0"))
			{
				String lat = Double.toString(address.getLatitude());
				String lon = Double.toString(address.getLongitude());
				
				
					
				new UltraFileaccess().Load_Data("edittxt1a",lat,v.getContext());
				new UltraFileaccess().Load_Data("edittxt1l",lon,v.getContext());	
				
			}
			if(POS.equals("1"))
			{
				String lat = Double.toString(address.getLatitude());
				String lon = Double.toString(address.getLongitude());
				
				
				
				new UltraFileaccess().Load_Data("edittxt2a",lat,v.getContext());
				new UltraFileaccess().Load_Data("edittxt2l",lon,v.getContext());
				
			}
			Intent i = new Intent(this, Fareccalculator9Activity.class);
			startActivity(i);
	      	
	    	 
	    	 
	    	
	    	
	    }
	    
	    private Runnable returnRes = new Runnable() {

	        @Override
	        public void run() {
	            if(m_orders != null && m_orders.size() > 0){
	                m_adapter.notifyDataSetChanged();
	                for(int i=0;i<m_orders.size();i++)
	                m_adapter.add(m_orders.get(i));
	            }
	            m_ProgressDialog.dismiss();
	            m_adapter.notifyDataSetChanged();
	        }
	    };
	    List<Address> addresses;
	    private void getOrders(){
	          try{
	        	  
	        	  
	              m_orders = new ArrayList<Order2>();
	              Geocoder geocoder = new Geocoder(searchlisters.this, Locale.getDefault());
	              try {
						addresses = geocoder.getFromLocationName(edittxt1.getText().toString(),20);
						
						 Address address = null;
						 for(int index=0; index<addresses.size(); ++index)
			                {
			                address = addresses.get(index);
			               
			                
			                	 Order2 o1 = new Order2();
			                o1.setOrderName(" "+address.getFeatureName()+","+address.getLocality());
			                o1.setOrderStatus(" "+address.getSubAdminArea()+","+address.getAdminArea()+","+address.getCountryName());
			                m_orders.add(o1);
			                
				              
			                
				              
			                }
						
						
					} catch (IOException e) {
						
						Toast.makeText(searchlisters.this,"no no no",Toast.LENGTH_SHORT).show();
					}
	              
	              
	             
	              
	             
	              
	             
	             
	              
	           
	              
	              
	              
	            } catch (Exception e) {
	              
	            }
	            runOnUiThread(returnRes);
	        }
	    
		private class OrderAdapter extends ArrayAdapter<Order2> {

	        private ArrayList<Order2> items;

	        public OrderAdapter(Context context, int textViewResourceId, ArrayList<Order2> items) {
	                super(context, textViewResourceId, items);
	                this.items = items;
	        }
	        @Override
	        public View getView(int position, View convertView, ViewGroup parent) {
	                View v = convertView;
	                if (v == null) {
	                    LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	                    v = vi.inflate(R.layout.rowx, null);
	                }
	                Order2 o = items.get(position);
	                if (o != null) {
	                        TextView tt = (TextView) v.findViewById(R.id.toptext);
	                        TextView bt = (TextView) v.findViewById(R.id.bottomtext);
	                        //ImageView ii = (ImageView) v.findViewById(R.id.);
	                        if (tt != null) {
	                              tt.setText(o.getOrderName());                            }
	                        if(bt != null){
	                              bt.setText(o.getOrderStatus());
	                        }
	                        
	                        
	                }
	                return v;
	        }
	}
	}