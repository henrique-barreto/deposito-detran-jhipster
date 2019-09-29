import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './deposito.reducer';
import { IDeposito } from 'app/shared/model/deposito.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDepositoDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class DepositoDetail extends React.Component<IDepositoDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { depositoEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Deposito [<b>{depositoEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="nome">Nome</span>
            </dt>
            <dd>{depositoEntity.nome}</dd>
            <dt>
              <span id="ativo">Ativo</span>
            </dt>
            <dd>{depositoEntity.ativo ? 'true' : 'false'}</dd>
          </dl>
          <Button tag={Link} to="/entity/deposito" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/deposito/${depositoEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ deposito }: IRootState) => ({
  depositoEntity: deposito.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DepositoDetail);
