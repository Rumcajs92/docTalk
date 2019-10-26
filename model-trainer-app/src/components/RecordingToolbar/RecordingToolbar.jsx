import RegularButton from "../CustomButtons/Button";
import { FiberManualRecord, Stop } from "@material-ui/icons";
import ButtonGroup from "@material-ui/core/ButtonGroup";
import React from "react";

export function RecordingToolbar(props) {
  const recordOrPlay = props.record ? (
    <RegularButton color={"info"} onClick={props.stopRecord}>
      <Stop /> Stop
    </RegularButton>
  ) : (
    <RegularButton color={"danger"} onClick={props.startRecord}>
      <FiberManualRecord /> Record
    </RegularButton>
  );
  return <ButtonGroup>{recordOrPlay}</ButtonGroup>;
}

export default RecordingToolbar;
