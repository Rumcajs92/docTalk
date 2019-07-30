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
// @material-ui/core
import withStyles from "@material-ui/core/styles/withStyles";
// @material-ui/icons
// core components

import dashboardStyle from "assets/jss/material-dashboard-react/views/dashboardStyle.jsx";
import SnackbarContent from "../../components/Snackbar/SnackbarContent";

class ModelTrainer extends React.Component {
  state = {
    value: 0
  };
  handleChange = (event, value) => {
    this.setState({ value });
  };

  handleChangeIndex = index => {
    this.setState({ value: index });
  };
  render() {
    // const { classes } = this.props;
    return (
      <div>
        <div>
          <SnackbarContent
            message={"Page under construction"}
            close
            color="info"
          />
        </div>
      </div>
    );
  }
}

ModelTrainer.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(dashboardStyle)(ModelTrainer);
