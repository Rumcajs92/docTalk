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
import GridItem from "components/Grid/GridItem.jsx";
import GridContainer from "components/Grid/GridContainer.jsx";
import Table from "components/Table/Table.jsx";
import Card from "components/Card/Card.jsx";
import CardHeader from "components/Card/CardHeader.jsx";
import CardBody from "components/Card/CardBody.jsx";

import dashboardStyle from "assets/jss/material-dashboard-react/views/dashboardStyle.jsx";
import SnackbarContent from "../../components/Snackbar/SnackbarContent";
import RegularButton from "../../components/CustomButtons/Button";
import { Link } from "react-router-dom";
import api from "../../api";

class AdaptationChooser extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: 0,
      selectedModel: null,
      error: null,
      modelTableHeaders: [],
      modelTableData: [],
      data: []
    };
    this.selectModel = this.selectModel.bind(this);
  }

  handleChange = (event, value) => {
    this.setState({ value });
  };

  handleChangeIndex = index => {
    this.setState({ value: index });
  };

  componentDidMount() {
    fetch(api.speechModels())
      .then(resp => {
        return resp.json();
      })
      .then(json => {
        let headers = ["id", "name"];
        let data = json.map(object => {
          return headers.map(header => {
            return object[header];
          });
        });
        this.setState({
          modelTableHeaders: headers,
          modelTableData: data,
          data: json
        });
      })
      .catch(error => {
        this.setState({
          error: (
            <SnackbarContent message={error.message} close color="danger" />
          )
        });
      });
  }

  selectModel(properties) {
    let foundModel = this.state.data.find(obj => {
      return obj.id === properties[this.state.modelTableHeaders.indexOf("id")];
    });
    let selectedModelComponent = (
      <div>
        <div>Id: {foundModel.id}</div>
        <div>Name: {foundModel.name}</div>
        <div>Description: {foundModel.desc}</div>
        <RegularButton
          component={Link}
          to={"/admin/adaptation-trainer/" + foundModel.id}
          com
          color={"info"}
        >
          Adapt
        </RegularButton>
      </div>
    );

    this.setState({
      selectedModel: selectedModelComponent
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
              {this.state.error && this.state.error}
              {this.state.modelTableData.length > 0 && (
                <Table
                  onRowClick={this.selectModel}
                  tableHeaderColor="primary"
                  tableHead={this.state.modelTableHeaders}
                  tableData={this.state.modelTableData}
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
            <CardBody>
              {this.state.selectedModel && this.state.selectedModel}
            </CardBody>
          </Card>
        </GridItem>
      </GridContainer>
    );
  }
}

AdaptationChooser.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(dashboardStyle)(AdaptationChooser);
