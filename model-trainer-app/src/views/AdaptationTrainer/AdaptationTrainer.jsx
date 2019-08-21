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

class AdaptationTrainer extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      modelId: props.match.params.modelId,
      modelData: null,
      error: null,
      uploadedFiles: [],
      uploadedFilesList: []
    };
    this.handleOnFileListChange = this.handleOnFileListChange.bind(this);
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

  handleOnFileListChange(acceptedFiles) {
    let concatArray = this.state.uploadedFiles.concat(acceptedFiles);
    this.setState({
      uploadedFiles: concatArray
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
                    label="Text to adapt"
                    multiline
                    rows="10"
                    defaultValue="Enter text"
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
                  <Dropzone
                    onDrop={acceptedFiles =>
                      this.handleOnFileListChange(acceptedFiles)
                    }
                  >
                    {({ getRootProps, getInputProps }) => (
                      <section>
                        <div {...getRootProps()}>
                          <input {...getInputProps()} />
                          <p>
                            Drag ann drop some files here, or click to select
                            files
                          </p>
                        </div>
                      </section>
                    )}
                  </Dropzone>
                  <div>
                    <UploadedFileComponent files={this.state.uploadedFiles} />
                  </div>
                </ExpansionPanelDetails>
              </ExpansionPanel>
            </CardBody>
          </Card>
        </GridContainer>
      </div>
    );
  }
}

function UploadedFileComponent(props) {
  if (props.files.length > 0) {
    return (
      <ul>
        {props.files.map(file => (
          <li key={file.name}>
            <div>name: {file.name} </div>
            <div>size: {file.size}</div>
          </li>
        ))}
      </ul>
    );
  }
  return null;
}


AdaptationTrainer.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(dashboardStyle)(AdaptationTrainer);
