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
// @material-ui/icons
// core components
import GridItem from "components/Grid/GridItem.jsx";
import GridContainer from "components/Grid/GridContainer.jsx";
import withStyles from "@material-ui/core/styles/withStyles";
import CardHeader from "../../components/Card/CardHeader";
import Card from "../../components/Card/Card";
import CardBody from "../../components/Card/CardBody";
import dashboardStyle from "../../assets/jss/material-dashboard-react/views/dashboardStyle";
import api from "../../api";
import SnackbarContent from "../../components/Snackbar/SnackbarContent";
import ExpansionPanelSummary from "@material-ui/core/ExpansionPanelSummary";
import ExpansionPanel from "@material-ui/core/ExpansionPanel";
import Typography from "@material-ui/core/Typography";
import ExpandMoreIcon from "@material-ui/icons/ExpandMore";
import ExpansionPanelDetails from "@material-ui/core/ExpansionPanelDetails";
import TextField from "@material-ui/core/TextField";
import Dropzone from "react-dropzone";
import Table from "../../components/Table/Table";
import { sizing } from "@material-ui/system";
import { Link } from "react-router-dom";
import RegularButton from "../../components/CustomButtons/Button";

class AdaptationTrainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      modelId: props.match.params.modelId,
      modelData: null,
      error: null,
      uploadedFiles: [],
      manuallyAdaptationTextInput: "Enter text"
    };
    this.handleOnFileListChange = this.handleOnFileListChange.bind(this);
    this.handleInputChange = this.handleInputChange.bind(this);
    this.handleAdapt = this.handleAdapt.bind(this);
  }

  componentDidMount() {
    fetch(api.speechModel(this.state.modelId))
      .then(resp => {
        return resp.json();
      })
      .then(json => {
        this.setState({
          modelData: json
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

  handleInputChange(event) {
    const target = event.target;
    const value = target.type === "checkbox" ? target.checked : target.value;
    const name = target.name;

    this.setState({
      [name]: value
    });
  }

  handleOnFileListChange(acceptedFiles) {
    let concatArray = this.state.uploadedFiles.concat(acceptedFiles);
    this.setState({
      uploadedFiles: concatArray
    });
  }

  handleAdapt() {
    const formData = new FormData();
    this.state.uploadedFiles.forEach(file => {
      formData.append("files[]", file, file.name);
    });
    formData.append("text", this.state.manuallyAdaptationTextInput);
    fetch(api.adaptationStart(this.state.modelId), {
      method: "POST",
      body: formData
    })
      .then(response => response.json())
      .then(json => {
        let adaptationId = json.id;
        this.props.history.push({
          pathname: "/admin/adaptation-recorder/" + adaptationId,
          state: {
            adaptation: json
          }
        });
      });
  }

  render() {
    const { classes } = this.props;
    return (
      <div>
        <GridContainer>
          <Card>
            <CardHeader color="primary">
              <h4 className={classes.cardTitleWhite}>Adaptation Details</h4>
            </CardHeader>
            <CardBody>
              {this.state.error && this.state.error}
              {this.state.modelData && (
                <Typography>
                  <div>
                    <div>id: {this.state.modelData.id}</div>
                    <div>name: {this.state.modelData.name}</div>
                    <div>desc: {this.state.modelData.desc}</div>
                  </div>
                </Typography>
              )}
            </CardBody>
          </Card>
        </GridContainer>
        <GridContainer>
          <Card>
            <CardHeader color="primary">
              <h4 className={classes.cardTitleWhite}>Input Text to adapt</h4>
            </CardHeader>
            <CardBody>
              <ExpansionPanel>
                <ExpansionPanelSummary
                  expandIcon={<ExpandMoreIcon />}
                  aria-controls="panel1a-content"
                  id="panel1a-header"
                >
                  <Typography className={classes.heading}>
                    Type text to adapt
                  </Typography>
                </ExpansionPanelSummary>
                <ExpansionPanelDetails>
                  <TextField
                    id="manually-adaptation-text-input"
                    onChange={this.handleInputChange}
                    name="manuallyAdaptationTextInput"
                    label="Text to adapt"
                    multiline
                    rows="10"
                    defaultValue={this.state.manuallyAdaptationTextInput}
                    className={classes.textField}
                    margin="normal"
                    fullWidth
                  />
                </ExpansionPanelDetails>
              </ExpansionPanel>
              <ExpansionPanel>
                <ExpansionPanelSummary
                  expandIcon={<ExpandMoreIcon />}
                  aria-controls="panel1a-content"
                  id="panel1a-header"
                >
                  <Typography className={classes.heading}>
                    Add files with text to adapt
                  </Typography>
                </ExpansionPanelSummary>
                <ExpansionPanelDetails>
                  <GridContainer width="100%">
                    <GridItem xs={12} sm={12} md={12}>
                      <Dropzone
                        onDrop={acceptedFiles =>
                          this.handleOnFileListChange(acceptedFiles)
                        }
                      >
                        {({ getRootProps, getInputProps }) => (
                          <section>
                            <div
                              {...getRootProps({
                                class: classes.dropZone,
                                width: "100%"
                              })}
                            >
                              <input {...getInputProps()} />
                              <p>
                                Drag ann drop some files here, or click to
                                select files
                              </p>
                            </div>
                          </section>
                        )}
                      </Dropzone>
                    </GridItem>
                    <GridItem xs={12} sm={12} md={12}>
                      <UploadedFileComponent files={this.state.uploadedFiles} />
                    </GridItem>
                  </GridContainer>
                </ExpansionPanelDetails>
              </ExpansionPanel>
              <RegularButton onClick={this.handleAdapt} color={"info"}>
                Adapt
              </RegularButton>
            </CardBody>
          </Card>
        </GridContainer>
      </div>
    );
  }
}

function UploadedFileComponent(props) {
  if (props.files.length > 0) {
    let headers = ["name", "size", "actions"];
    let data = props.files.map(file => [file.name, file.size, "actions tools"]);
    return (
      <Table
        onRowClick={() => {}}
        tableHeaderColor="primary"
        tableHead={headers}
        tableData={data}
      />
    );
  }
  return null;
}

AdaptationTrainer.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(dashboardStyle)(AdaptationTrainer);
