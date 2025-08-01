import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './recensione.reducer';

export const RecensioneDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const recensioneEntity = useAppSelector(state => state.recensione.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="recensioneDetailsHeading">Recensione</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{recensioneEntity.id}</dd>
          <dt>
            <span id="descrizione">Descrizione</span>
          </dt>
          <dd>{recensioneEntity.descrizione}</dd>
          <dt>Libro</dt>
          <dd>{recensioneEntity.libro ? recensioneEntity.libro.id : ''}</dd>
          <dt>User</dt>
          <dd>{recensioneEntity.user ? recensioneEntity.user.login : ''}</dd>
        </dl>
        <Button tag={Link} to="/recensione" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/recensione/${recensioneEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default RecensioneDetail;
