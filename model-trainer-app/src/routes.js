/*!

=========================================================
* Material Dashboard React - v1.7.0
=========================================================

* Product Page: https://www.creative-tim.com/product/material-dashboard-react
* Copyright 2019 Creative Tim (https://www.creative-tim.com)
* Licensed under MIT (https://github.com/creativetimofficial/material-dashboard-react/blob/master/LICENSE.md)

* Coded by Creative Tim

=========================================================

* The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

*/
// @material-ui/icons
import Edit from "@material-ui/icons/Edit";
import RecordVoiceOver from "@material-ui/icons/RecordVoiceOver";
import SurroundSound from "@material-ui/icons/SurroundSound";
// core components/views for Admin layout
import AdaptationChooser from "./views/AdaptationChooser/AdaptationChooser.jsx";
import AdaptationTrainer from "./views/AdaptationTrainer/AdaptationTrainer.jsx";
import Editor from "./views/Editor/Editor";
import ModelTrainer from "./views/ModelTrainer/ModelTrainer";

const dashboardRoutes = [
  {
    path: "/editor",
    name: "Editor",
    icon: Edit,
    component: Editor,
    layout: "/admin",
    navbar: true
  },
  {
    path: "/adaptation-chooser",
    name: "Adaptation Chooser",
    icon: RecordVoiceOver,
    component: AdaptationChooser,
    layout: "/admin",
    navbar: true
  },
  {
    path: "/adaptation-trainer/:modelId",
    name: "Adaptation Trainer",
    icon: RecordVoiceOver,
    component: AdaptationTrainer,
    layout: "/admin",
    navbar: false
  },
  {
    path: "/model-trainer",
    name: "Model Trainer",
    icon: SurroundSound,
    component: ModelTrainer,
    layout: "/admin",
    navbar: true
  }
];

export default dashboardRoutes;
