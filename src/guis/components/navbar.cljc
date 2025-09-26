(ns guis.components.navbar)


(defn responsive-navbar []
  [:div
   {:class "navbar bg-base-100 shadow-sm"}
   [:div
    {:class "flex-1"}
    [:a {:class "btn btn-ghost text-xl"
         :on {:click [[:gui/navigate {:location ""}]]}} [:img {:src "images/ganum-logo.png"
                                                :alt "Ganum logo"
                                                :width "30px"}]
     "Ganum"]]
   [:div
    {:class "flex-none"}
    [:ul
     {:class "menu menu-horizontal px-1"}
     [:li [:a {:on {:click [[:gui/navigate {:location "/analyze"}]]}} "Analyze"]]
     [:li [:a {:on {:click [[:gui/navigate {:location "/files"}]]}} "Files"]]]]])

