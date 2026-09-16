# Problem Statement & System Scope

## 1. Problem Statement
During humanitarian crises, natural disasters, and large-scale emergency evacuations, the breakdown of communication and logistics infrastructure leads to critical resource mismatches. High-density relief camps frequently endure life-threatening deficits of vital medical supplies, infant formula, and potable water, while secondary shelters accumulate unneeded surpluses due to uncoordinated donation pipelines. Existing manual methods (such as ad-hoc spreadsheets and disconnected phone logs) lack real-time visibility, fail to prioritize distribution based on clinical urgency, and offer zero traceability for dispatched aid consignments.

## 2. Scope of the Project
The **Disaster Relief & Supply Chain Allocation Engine** is a specialized logistics coordination system engineered to automate emergency supply triage. 

### In-Scope:
- **Centralized Stock Ingestion:** Categorized tracking of available inventory across critical survival verticals (Medical, Food, Water, Shelter).
- **Triage & Urgency Prioritization:** Dynamic queue management evaluating camp distress levels (`CRITICAL`, `HIGH`, `MEDIUM`, `LOW`) using weighted algorithmic scheduling.
- **Automated Resource Allocation:** Greedy matching algorithm executing real-time stock deductions against priority demand queues.
- **Audit Logging & Dispatch Manifests:** Generation of immutable chronological distribution logs and automated CSV manifests for logistical transport units.
- **Interactive Monitoring Dashboard:** Desktop GUI facilitating rapid situational analysis for dispatch commanders.

### Out-of-Scope:
- Direct hardware GPS tracking of vehicles in transit.
- Payment gateway integration for monetary donations.

## 3. Target Users
- **National & State Disaster Management Authorities (NDMA / SDMA):** Strategic decision-makers monitoring macroscopic resource availability across regional sectors.
- **Humanitarian Logistics Coordinators:** Warehouse managers supervising receipt of bulk donations and packaging outbound convoys.
- **On-site Relief Camp Directors:** Field personnel submitting distress requests based on shifting camp demographics and local medical emergencies.

## 4. High-Level Features
- **Dynamic Priority-Queue Matching:** Implements a max-heap data structure sorting incoming relief demands by severity weight to prevent triage starvation.
- **Atomic Stock Reservation:** Guarantees inventory counts update immediately upon dispatch generation, preventing accidental double-allocation of stock.
- **Real-Time Desktop Dashboard:** Dual-table Swing dashboard displaying real-time warehouse inventory alongside categorized camp demands.
- **Standardized Data Export:** One-click CSV manifest generation ensuring compatibility with third-party logistics tools and field driver manifests.
