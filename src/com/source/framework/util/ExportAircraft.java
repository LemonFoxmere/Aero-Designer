package com.source.framework.util;

import java.io.FileWriter;
import java.io.IOException;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.source.excEnv.model.shape.vStabalizer;
import com.source.excEnv.model.shape.wing;

public class ExportAircraft {
	
	@SuppressWarnings("unchecked")
    public static void exportDefaultParam(){
		JSONObject controls_objects = new JSONObject();
			
			JSONObject is_symmetric = new JSONObject();
				is_symmetric.put("is_symmetric", true);
			JSONObject is_not_symmetric = new JSONObject();
				is_not_symmetric.put("is_symmetric", false);
			
			controls_objects.put("aileron", is_not_symmetric);		
			controls_objects.put("elevator", is_symmetric);
			controls_objects.put("rudder", is_not_symmetric);	
			//put all controls objects together under the name: control
		
		//----------------------------------------------------------------------------------------------------

		JSONObject NACA_objects = new JSONObject();
			NACA_objects.put("type", "linear");
			NACA_objects.put("aL0", 0.0);
			NACA_objects.put("CLa", 6.4336);
			NACA_objects.put("CmL0", 0.0);
			NACA_objects.put("Cma", 0.00);
			NACA_objects.put("CD0", 0.00513);
			NACA_objects.put("CD1", 0.0);
			NACA_objects.put("CD2", 0.0984);
			NACA_objects.put("CL_max", 1.4);
		
		JSONObject NACA_2412 = new JSONObject();
			NACA_2412.put("NACA_2412", NACA_objects);			
		
		//----------------------------------------------------------------------------------------------------
		
			JSONObject control_surface_main = new JSONObject();
				control_surface_main.put("chord_fraction", 0.2);
					JSONObject chord_mixing_main = new JSONObject();
					chord_mixing_main.put("aileron", 1.0);
				control_surface_main.put("control_mixing", chord_mixing_main);
				
			JSONObject main_wing_objects = new JSONObject();
				main_wing_objects.put("ID", 1);
				main_wing_objects.put("side", "both");
				main_wing_objects.put("is_main", true);
				main_wing_objects.put("semispan", 2.0);
				
					JSONArray chord = new JSONArray();
						JSONArray chorda = new JSONArray();
							chorda.add(0.0); chorda.add(0.5);
						JSONArray chordb = new JSONArray();
							chordb.add(1.0); chordb.add(0.4);
					chord.add(chorda); chord.add(chordb);
				
				main_wing_objects.put("chord", chord);
				main_wing_objects.put("airfoil", "NACA_2412");
				main_wing_objects.put("control_surface", control_surface_main);
					JSONObject grid_main = new JSONObject();
					grid_main.put("N", 40);
				main_wing_objects.put("grid", grid_main);
				
		//----------------------------------------------------------------------------------------------------
				
			JSONObject connect_to_hstab = new JSONObject();
				connect_to_hstab.put("ID", 1);
				connect_to_hstab.put("location", "root");
				connect_to_hstab.put("dx", -1.0);		
				
			JSONObject control_surface_hstab = new JSONObject();
				control_surface_hstab.put("chord_fraction", 0.5);
					JSONObject chord_mixing_hstab = new JSONObject();
					chord_mixing_hstab.put("elevator", 1.0);
				control_surface_hstab.put("control_mixing", chord_mixing_hstab);

				
			JSONObject h_stab_objects = new JSONObject();
				h_stab_objects.put("ID", 2);
				h_stab_objects.put("side", "both");

				h_stab_objects.put("is_main", false);
				h_stab_objects.put("connect_to", connect_to_hstab);
				h_stab_objects.put("semispan", 0.5);
				h_stab_objects.put("sweep", 20.0);
				h_stab_objects.put("chord", 0.4);
				h_stab_objects.put("airfoil", "NACA_2412");
				h_stab_objects.put("control_surface", control_surface_hstab);
					JSONObject grid_h = new JSONObject();
					grid_h.put("N", 40);
				h_stab_objects.put("grid", grid_h);
				
			//----------------------------------------------------------------------------------------------------
				
			JSONObject connect_to_vstab = new JSONObject();
				connect_to_vstab.put("ID", 1);
				connect_to_vstab.put("location", "root");
				connect_to_vstab.put("dx", -1.0);		
				connect_to_vstab.put("dz", -0.1);		
				
			JSONObject control_surface_vstab = new JSONObject();
				control_surface_vstab.put("chord_fraction", 0.5);
					JSONObject chord_mixing_vstab = new JSONObject();
					chord_mixing_vstab.put("rudder", 1.0);
				control_surface_vstab.put("control_mixing", chord_mixing_vstab);
				
			JSONObject v_stab_objects = new JSONObject();
				v_stab_objects.put("ID", 3);
				v_stab_objects.put("side", "left");
				v_stab_objects.put("is_main", false);
				v_stab_objects.put("connect_to", connect_to_vstab);
				v_stab_objects.put("semispan", 0.5);
				v_stab_objects.put("sweep", 20.0);
				v_stab_objects.put("chord", 0.4);
				v_stab_objects.put("dihedral", 90);
				v_stab_objects.put("airfoil", "NACA_2412");
				v_stab_objects.put("control_surface", control_surface_vstab);
					JSONObject grid_v = new JSONObject();
					grid_v.put("N", 40);
				v_stab_objects.put("grid", grid_v);

				
			//----------------------------------------------------------------------------------------------------
				
			JSONObject wing_objects = new JSONObject();
			wing_objects.put("main_wing", main_wing_objects);
			wing_objects.put("h_stab", h_stab_objects);
			wing_objects.put("v_stab", v_stab_objects);
			
			//----------------------------------------------------------------------------------------------------
			
		//final result
		JSONObject result = new JSONObject();
			JSONArray CG = new JSONArray();
			CG.add(0); CG.add(0); CG.add(0);
		result.put("CG", CG);
		result.put("weight", 20.0);
		result.put("reference", new JSONObject());
		result.put("controls", controls_objects);
		result.put("airfoils", NACA_2412);
		result.put("wings", wing_objects);
		
		//Write JSON file
		try (FileWriter file = new FileWriter("airplane.json")) {
			file.write(result.toJSONString());
			file.flush();
		} catch (IOException e) { 
			e.printStackTrace();
		}
    }

	@SuppressWarnings("unchecked")
    public static void exportAircraft(wing mainWing, wing hStab, vStabalizer vStab){
		JSONObject controls_objects = new JSONObject();
		
			JSONObject is_symmetric = new JSONObject();
				is_symmetric.put("is_symmetric", true);
			JSONObject is_not_symmetric = new JSONObject();
				is_not_symmetric.put("is_symmetric", false);
			
			controls_objects.put("aileron", is_not_symmetric);		
			controls_objects.put("elevator", is_symmetric);
			controls_objects.put("rudder", is_not_symmetric);	
			//put all controls objects together under the name: control
		
		//----------------------------------------------------------------------------------------------------

		JSONObject NACA_objects = new JSONObject();
			NACA_objects.put("type", "linear");
			NACA_objects.put("aL0", 0.0);
			NACA_objects.put("CLa", 6.4336);
			NACA_objects.put("CmL0", 0.0);
			NACA_objects.put("Cma", 0.00);
			NACA_objects.put("CD0", 0.00513);
			NACA_objects.put("CD1", 0.0);
			NACA_objects.put("CD2", 0.0984);
			NACA_objects.put("CL_max", 1.4);
		
		JSONObject NACA_2412 = new JSONObject();
			NACA_2412.put("NACA_2412", NACA_objects);			
		
		//----------------------------------------------------------------------------------------------------
		
			JSONObject control_surface_main = new JSONObject();
				control_surface_main.put("chord_fraction", 0.2);
					JSONObject chord_mixing_main = new JSONObject();
					chord_mixing_main.put("aileron", 1.0);
				control_surface_main.put("control_mixing", chord_mixing_main);
				
			JSONObject main_wing_objects = new JSONObject();
				main_wing_objects.put("ID", 1);
				main_wing_objects.put("side", "both");
				main_wing_objects.put("is_main", true);
				main_wing_objects.put("semispan", mainWing.semiSpan/10); // changed
				main_wing_objects.put("sweep", mainWing.sweep); // added
				main_wing_objects.put("dihedral", mainWing.dihedral); // added
				main_wing_objects.put("angle_of_attack", mainWing.aoa); // added
		/*
		 * JSONArray chord = new JSONArray(); // TODO JSONArray chorda = new
		 * JSONArray(); chorda.add(0.0); chorda.add(0.5); JSONArray chordb = new
		 * JSONArray(); chordb.add(1.0); chordb.add(0.4); chord.add(chorda);
		 * chord.add(chordb);
		 */
//				main_wing_objects.put("chord", chord);
				main_wing_objects.put("chord", mainWing.chord/12);
				main_wing_objects.put("airfoil", "NACA_2412"); // changed
				main_wing_objects.put("control_surface", control_surface_main);
					JSONObject grid_main = new JSONObject();
					grid_main.put("N", 40);
				main_wing_objects.put("grid", grid_main);
				
		//----------------------------------------------------------------------------------------------------
				
			JSONObject connect_to_hstab = new JSONObject();
				connect_to_hstab.put("ID", 1);
				connect_to_hstab.put("location", "root");
				connect_to_hstab.put("dx", -1.0);		
				
			JSONObject control_surface_hstab = new JSONObject();
				control_surface_hstab.put("chord_fraction", 0.5);
					JSONObject chord_mixing_hstab = new JSONObject();
					chord_mixing_hstab.put("elevator", 1.0);
				control_surface_hstab.put("control_mixing", chord_mixing_hstab);

				
			JSONObject h_stab_objects = new JSONObject();
				h_stab_objects.put("ID", 2);
				h_stab_objects.put("side", "both");

				h_stab_objects.put("is_main", false);
				h_stab_objects.put("connect_to", connect_to_hstab);
				h_stab_objects.put("semispan", hStab.semiSpan/10);
				h_stab_objects.put("sweep", hStab.sweep);
				h_stab_objects.put("chord", hStab.chord/12);
				h_stab_objects.put("angle_of_attack", hStab.aoa);
				h_stab_objects.put("dihedral", hStab.dihedral);
				h_stab_objects.put("airfoil", "NACA_2412");
				h_stab_objects.put("control_surface", control_surface_hstab);
					JSONObject grid_h = new JSONObject();
					grid_h.put("N", 40);
				h_stab_objects.put("grid", grid_h);
				
			//----------------------------------------------------------------------------------------------------
				
			JSONObject connect_to_vstab = new JSONObject();
				connect_to_vstab.put("ID", 1);
				connect_to_vstab.put("location", "root");
				connect_to_vstab.put("dx", -1.0);		
				connect_to_vstab.put("dz", -0.1);		
				
			JSONObject control_surface_vstab = new JSONObject();
				control_surface_vstab.put("chord_fraction", 0.5);
					JSONObject chord_mixing_vstab = new JSONObject();
					chord_mixing_vstab.put("rudder", 1.0);
				control_surface_vstab.put("control_mixing", chord_mixing_vstab);
				
			JSONObject v_stab_objects = new JSONObject();
				v_stab_objects.put("ID", 3);
				v_stab_objects.put("side", "left");
				v_stab_objects.put("is_main", false);
				v_stab_objects.put("connect_to", connect_to_vstab);
				v_stab_objects.put("semispan", vStab.wingSpan/10);
				v_stab_objects.put("sweep", vStab.sweep);
				v_stab_objects.put("chord", vStab.chord/12);
				v_stab_objects.put("airfoil", "NACA_2412");
				v_stab_objects.put("control_surface", control_surface_vstab);
					JSONObject grid_v = new JSONObject();
					grid_v.put("N", 40);
				v_stab_objects.put("grid", grid_v);

				
			//----------------------------------------------------------------------------------------------------
				
			JSONObject wing_objects = new JSONObject();
			wing_objects.put("main_wing", main_wing_objects);
			wing_objects.put("h_stab", h_stab_objects);
			wing_objects.put("v_stab", v_stab_objects);
			
			//----------------------------------------------------------------------------------------------------
			
		//final result
		JSONObject result = new JSONObject();
			JSONArray CG = new JSONArray();
			CG.add(0); CG.add(0); CG.add(0);
		result.put("CG", CG);
		result.put("weight", 20.0);
		result.put("reference", new JSONObject());
		result.put("controls", controls_objects);
		result.put("airfoils", NACA_2412);
		result.put("wings", wing_objects);
		
		//Write JSON file
		try (FileWriter file = new FileWriter("airplane.json")) {
			file.write(result.toJSONString());
			file.flush();
		} catch (IOException e) { 
			e.printStackTrace();
		}
    }
}
