//package com.example.modulus.Model;
//
//import com.example.modulus.FragmentInsights.InsightsFragment;
//
//import java.util.List;
//
//public class Kahn {
//
//    List<ModuleVertex> adjList;
//    public void setUpGraph(){
//        for(ModuleModel mod: InsightsFragment.moduleList){
//            adjList.add(new ModuleVertex(mod));
//        }
//    }
//    public List<ModuleModel> RestrictedSet(Inputs){
//
//    }
//1.	Required ← PillarCore ∪ TrackCore ∪ MinorCore ∪ TrackElectives ∪ MinorElectives
//2.	R ← Empty Set
//3.	for each module v in G.V
//    // Add module if its in required
//4.		if v in Required
//5.			Add v to R
//6.		//Add module if its in required’s modules prerequisites
//        7.		Else if  isPREREQRequired(v,Required)
//8.			Add v to R
//9.	Return R
//    function isPREREQRequired(v,Required)
//1.	for each module u in Required
//2.		for each module m in u.hard
//3.			if m == v
//4.			Return TRUE
//5.		for each module m in u.soft
//6.			if m == v
//7.			Return TRUE
//8.
//        9.	Return FALSE
//
//    function Planner(Inputs)
//1.	All ← Array to store all permutations
//2.	Schedule ← Array of 5 for 5 terms  (A[1 (Term 4)] …  A[5 (Term 8)])
//            3.	HASSflag ← Boolean to check got pending HASS
//4.	CurrentTerm ← 1 (Term 4)
//            5.	Order ← Empty Array for modules (for adding on per term)
//6.	inDeg ← Array for all of v’s indegree
//7.	R ← RestrictedSet(Inputs)
//8.	PERMUTE(Inputs,All,Schedule,HASSflag,Order,inDeg, CurrentTerm,R)
//9.	Return ALL
//
//    function PERMUTE(Inputs,All,Schedule,HASSflag,Order,inDeg,CurrentTerm,R)
//    // Base case when permutations for all terms done
//1.	if currentTerm > TotalTerms then
//2.		if (PillarCore ⊆ Order & TrackCore ⊆ Order & MinorCore ⊆ Order & count(TrackElectives ∩ Order) >= TrackElectivesCount Order & count(MinorElectives ∩ Order) >= MinorElectivesCount ) then
//3.		Append Order.copy to All
//4.		return
////Prepare the current term list
//        5.	Current_list ← Empty Array
//6.	Schedule[currentTerm] ← Current_list
////Fill current term
//7.	FILL(Inputs,All,Schedule,HASSflag,Order,inDeg,CurrentTerm,0,R)
//8.	return
//
//        0 is CountCapacity to track if TermCapacity is full
//
//    function FILL(Inputs,All,Schedule,HASSflag,Order,inDeg, CurrentTerm,CountCapacity,R)
//    // Base case when current term is full , need go next term
//1.	if CountCapacity == TermCapacity then
//    //Need to check if got HASS (I hate HASS)
//2.		HasHASS ← Boolean TRUE if count of v.type == “HASS” in schedule[currentTerm] == 1
//            3.		PendingHASS ← Boolean TRUE if HasHASS == FALSE
//    //Append to current term modules if valid ordering
//4.	 	NewOrder ← Concatenate Order with Schedule[CurrentTerm]
//            5.		// Recursive loop into next term
//            6.		PERMUTE(Inputs,All,Schedule,PendingHASS (HASSflag changed to this),Order,inDeg, CurrentTerm+1,R)
//            7.		Return
//8.	// Not full, need add module while checking requirements
//        9.	for each module v in R not in Order
//10.		//Check if indegree 0 to allow adding and prereq is met
//        11.		if inDeg[v.index] == 0 and PREREQCHECK(v,Order,G)
//12.			//Enforce HASS if Pending and current is HASS
//        if HASSflag and v.type ≠ “HASS”
//            13.				continue
//            14.			//Appending to current term
//            15.			Append v to schedule[CurrentTerm]
//            16.			Append to Order
//    //explore the neighbours in v
//17.			u ← G.Adj[v].head
//18.			while u ≠ NIL do
//            19.				inDeg[u.index] ← inDeg[u.index] - 1
//            20.				u ← u.next
//9.			FILL(Inputs,All,Schedule,HASSflag,Order,inDeg, CurrentTerm,CountCapacity+1,R))
////backtracking
//            21.			u ← G.Adj[v].head
//22.			while u ≠ NIL do
//            23.				inDeg[u.index] ← inDeg[u.index] + 1
//            24.				u ← u.next
//25.			Remove v from schedule[CurrentTerm]
//            26.			Remove v from  Order
//27.	Return
//
//
//
//    function PREREQCHECK(v,Order,G)
//    // Check hard prerequisites
//1.	for each module u in v.hard
//2.		if u not in Order
//3.			return FALSE
//// Check soft prerequisites
//4.	actualCount ← 0
//            5.	for each module u in v.soft
//6.		if u in Order
//7.			actualCount ← actualCount + 1
//            8.	if actualCount < v.soft_count
//9.		return FALSE
//10.	return TRUE
//
//}
