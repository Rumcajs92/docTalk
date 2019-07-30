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
import React from "react";
// nodejs library to set properties for components
import PropTypes from "prop-types";
// react plugin for creating charts
import ChartistGraph from "react-chartist";
// @material-ui/core
import withStyles from "@material-ui/core/styles/withStyles";
import Icon from "@material-ui/core/Icon";
// @material-ui/icons
import Store from "@material-ui/icons/Store";
import Warning from "@material-ui/icons/Warning";
import DateRange from "@material-ui/icons/DateRange";
import LocalOffer from "@material-ui/icons/LocalOffer";
import Update from "@material-ui/icons/Update";
import ArrowUpward from "@material-ui/icons/ArrowUpward";
import AccessTime from "@material-ui/icons/AccessTime";
import Accessibility from "@material-ui/icons/Accessibility";
import BugReport from "@material-ui/icons/BugReport";
import Code from "@material-ui/icons/Code";
import Cloud from "@material-ui/icons/Cloud";
// core components
import GridItem from "components/Grid/GridItem.jsx";
import GridContainer from "components/Grid/GridContainer.jsx";
import Table from "components/Table/Table.jsx";
import Tasks from "components/Tasks/Tasks.jsx";
import CustomTabs from "components/CustomTabs/CustomTabs.jsx";
import Danger from "components/Typography/Danger.jsx";
import Card from "components/Card/Card.jsx";
import CardHeader from "components/Card/CardHeader.jsx";
import CardIcon from "components/Card/CardIcon.jsx";
import CardBody from "components/Card/CardBody.jsx";
import CardFooter from "components/Card/CardFooter.jsx";

import { bugs, website, server } from "variables/general.jsx";

import {
  dailySalesChart,
  emailsSubscriptionChart,
  completedTasksChart
} from "variables/charts.jsx";

import dashboardStyle from "assets/jss/material-dashboard-react/views/dashboardStyle.jsx";
import SnackbarContent from "../../components/Snackbar/SnackbarContent";
import { AddAlert } from "@material-ui/icons";

class AdaptationTrainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: 0,
      models: [],
      selectedModel: null
    };
  }

  handleChange = (event, value) => {
    this.setState({ value });
  };

  handleChangeIndex = index => {
    this.setState({ value: index });
  };

  componentDidMount() {
    let api = "http://localhost:8080";

    fetch(api + "/speech-models")
      .then(resp => {
        return resp.json();
      })
      .then(json => {
        this.setState({
          models: json
        });
      });

  }

  render() {
    const { classes } = this.props;
    return (
      <GridContainer>
        <GridItem xs={12} sm={12} md={6}>
          <Card>
            <CardHeader color="primary">
              <h4 className={classes.cardTitleWhite}>
                List of models available to adapt
              </h4>
              <p className={classes.cardCategoryWhite}>
                This page adapts already existing acoustic models to a user's
                voice
              </p>
            </CardHeader>
            <CardBody>
              {this.state.models.length > 0 && (
                <Table
                  oncli
                  tableHeaderColor="primary"
                  tableHead={Object.keys(this.state.models[0])}
                  tableData={this.state.models.map(value =>
                    Object.values(value)
                  )}
                />
              )}
            </CardBody>
          </Card>
        </GridItem>
        <GridItem xs={12} sm={12} md={6}>
          <Card>
            <CardHeader color="primary">
              <h4 className={classes.cardTitleWhite}>Model details</h4>
              <p className={classes.cardCategoryWhite}>
                This page adapts already existing acoustic models to a user's
                voice
              </p>
            </CardHeader>
            <CardBody></CardBody>
          </Card>
        </GridItem>
      </GridContainer>
    );
  }
}

AdaptationTrainer.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(dashboardStyle)(AdaptationTrainer);
